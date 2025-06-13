package lab7.main.java.command;

import lab6.main.java.client.Client;
import lab6.main.java.command.AbstractCommand;
import lab6.main.java.data.LabWork;
import lab6.main.java.network.CommandRequest;
import lab6.main.java.network.CommandResponse;
import lab6.main.java.util.InputManager;
import lab6.main.java.util.OutputManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ExecuteScriptCommand extends AbstractCommand {
    private final InputManager inputManager;
    private final OutputManager outputManager;
    private final Client client;
    private static final int MAX_NESTING_LEVEL = 10;
    private static final ThreadLocal<List<String>> executedScripts = ThreadLocal.withInitial(ArrayList::new);

    public ExecuteScriptCommand(Client client, InputManager inputManager, OutputManager outputManager) {
        super("execute_script", "execute_script <file_name> : execute commands from a file");
        this.client = client;
        this.inputManager = inputManager;
        this.outputManager = outputManager;
    }

    @Override
    public CommandResponse execute(String argument, Object... args) {
        if (argument == null || argument.trim().isEmpty()) {
            return new CommandResponse(false, "Usage: execute_script <file_name>", null);
        }

        try {
            Path scriptPath = Paths.get("src/main/java/lab6/resources", argument).toAbsolutePath().normalize();
            outputManager.println("Looking for file at: " + scriptPath);

            // Проверка на рекурсию
            List<String> currentScripts = executedScripts.get();
            String scriptPathStr = scriptPath.toString();
            if (currentScripts.contains(scriptPathStr)) {
                return new CommandResponse(false, "Recursion detected: script " + argument + " is already being executed.", null);
            }
            if (currentScripts.size() >= MAX_NESTING_LEVEL) {
                return new CommandResponse(false, "Maximum nesting level (" + MAX_NESTING_LEVEL + ") exceeded.", null);
            }

            // Добавляем текущий скрипт в список выполненных
            currentScripts.add(scriptPathStr);
            try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath.toFile()))) {
                Queue<String> scriptLines = new LinkedList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    scriptLines.add(line);
                }

                while (!scriptLines.isEmpty()) {
                    String commandLine = scriptLines.poll().trim();
                    if (commandLine.isEmpty()) continue;

                    outputManager.println("Executing script command: " + commandLine);
                    String[] parts = commandLine.split("\\s+", 2);
                    String commandName = parts[0].toLowerCase();
                    String commandArg = parts.length > 1 ? parts[1] : "";

                    CommandRequest commandRequest;
                    // Обработка команд
                    if (commandName.equals("add")) {
                        inputManager.setScriptLines(new LinkedList<>(scriptLines));
                        try {
                            LabWork labWork = inputManager.getLabWorkFromInput();
                            if (labWork == null) {
                                outputManager.println("Failed to create LabWork from script.");
                                return new CommandResponse(false, "Error parsing command from script: " + commandLine, null);
                            }
                            scriptLines.clear();
                            scriptLines.addAll(inputManager.getScriptLines());
                            commandRequest = new CommandRequest(commandName, "", labWork);
                        } catch (Exception e) {
                            outputManager.println("Error creating LabWork from script: " + e.getMessage());
                            return new CommandResponse(false, "Error parsing command from script: " + commandLine, null);
                        } finally {
                            inputManager.clearScriptLines();
                        }
                    } else if (commandName.equals("update")) {
                        // Проверка наличия аргумента id
                        if (commandArg == null || commandArg.trim().isEmpty()) {
                            outputManager.println("Usage: update <id>");
                            return new CommandResponse(false, "Error parsing command from script: " + commandLine, null);
                        }
                        // Проверка валидности id
                        long id;
                        try {
                            id = Long.parseLong(commandArg);
                        } catch (NumberFormatException e) {
                            outputManager.println("Invalid id format: " + commandArg);
                            return new CommandResponse(false, "Error parsing command from script: " + commandLine, null);
                        }
                        // Проверка существования LabWork с id
                        CommandRequest showRequest = new CommandRequest("show", "");
                        CommandResponse showResponse = client.sendRequest(showRequest);
                        if (showResponse == null || !showResponse.isSuccess()) {
                            outputManager.println(showResponse != null ? showResponse.getMessage() : "Failed to retrieve collection.");
                            return new CommandResponse(false, "Error parsing command from script: " + commandLine, null);
                        }
                        List<Object> labWorks = showResponse.getData();
                        boolean idExists = false;
                        if (labWorks != null) {
                            for (Object obj : labWorks) {
                                if (obj instanceof LabWork labWork && labWork.getId() == id) {
                                    idExists = true;
                                    break;
                                }
                            }
                        }
                        if (!idExists) {
                            outputManager.println("No LabWork with id " + id + " found.");
                            return new CommandResponse(false, "Error parsing command from script: " + commandLine, null);
                        }
                        // Создание LabWork
                        inputManager.setScriptLines(new LinkedList<>(scriptLines));
                        try {
                            LabWork labWork = inputManager.getLabWorkFromInput();
                            if (labWork == null) {
                                outputManager.println("Failed to create LabWork from script.");
                                return new CommandResponse(false, "Error parsing command from script: " + commandLine, null);
                            }
                            scriptLines.clear();
                            scriptLines.addAll(inputManager.getScriptLines());
                            commandRequest = new CommandRequest(commandName, commandArg, labWork);
                        } catch (Exception e) {
                            outputManager.println("Error creating LabWork from script: " + e.getMessage());
                            return new CommandResponse(false, "Error parsing command from script: " + commandLine, null);
                        } finally {
                            inputManager.clearScriptLines();
                        }
                    } else if (commandName.equals("execute_script")) {
                        CommandResponse nestedResponse = execute(commandArg);
                        if (!nestedResponse.isSuccess()) {
                            return nestedResponse;
                        }
                        continue;
                    } else {
                        commandRequest = new CommandRequest(commandName, commandArg);
                    }

                    // Отправка команды на сервер
                    CommandResponse response = client.sendRequest(commandRequest);
                    if (response == null) {
                        return new CommandResponse(false, "No response from server for command: " + commandLine, null);
                    }
                    outputManager.println(response.getMessage());
                    // Безопасная обработка data
                    List<Object> responseData = response.getData();
                    if (responseData != null && !responseData.isEmpty()) {
                        responseData.forEach(item -> {
                            if (item != null) {
                                outputManager.println(item.toString());
                            }
                        });
                    }
                    if (!response.isSuccess()) {
                        return new CommandResponse(false, "Error executing command from script: " + commandLine, null);
                    }
                }

                return new CommandResponse(true, "Script executed successfully!", null);
            } finally {
                // Удаляем скрипт из списка после выполнения
                currentScripts.remove(scriptPathStr);
            }
        } catch (IOException e) {
            return new CommandResponse(false, "Error reading script file: " + e.getMessage(), null);
        } catch (Exception e) {
            return new CommandResponse(false, "Error executing script: " + e.getMessage(), null);
        }
    }
}