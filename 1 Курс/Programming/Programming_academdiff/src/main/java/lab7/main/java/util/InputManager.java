package lab7.main.java.util;

import lab7.main.java.data.*;
import lab7.main.java.exception.InvalidDataException;
import lab7.main.java.util.OutputManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class InputManager {
    private final BufferedReader reader;
    private final OutputManager outputManager;
    private Queue<String> scriptLines = new LinkedList<>();

    public InputManager(OutputManager outputManager) {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.outputManager = outputManager;
    }

    public void setScriptLines(Queue<String> lines) {
        this.scriptLines = new LinkedList<>(lines);
    }

    public void clearScriptLines() {
        this.scriptLines.clear();
    }

    public Queue<String> getScriptLines() {
        return new LinkedList<>(scriptLines);
    }

    public String readLine(String prompt) {
        outputManager.print(prompt);
        try {
            if (!scriptLines.isEmpty()) {
                String line = scriptLines.poll();
                outputManager.println(line);
                return line;
            }
            return reader.readLine();
        } catch (IOException e) {
            outputManager.println("Error reading input: " + e.getMessage());
            return null;
        }
    }

    public String getStringInput(String prompt, boolean nullable) {
        while (true) {
            String input = readLine(prompt);
            if (input == null || input.trim().isEmpty()) {
                if (nullable) {
                    return null;
                } else {
                    outputManager.println("This field cannot be empty.");
                    if (!scriptLines.isEmpty()) {
                        return null; // Прерываем для скрипта
                    }
                    continue; // Запрашиваем заново в интерактивном режиме
                }
            }
            return input;
        }
    }

    public Integer getIntInput(String prompt, int min) {
        while (true) {
            String input = readLine(prompt);
            if (input == null || input.trim().isEmpty()) {
                outputManager.println("This field cannot be empty.");
                if (!scriptLines.isEmpty()) {
                    return null;
                }
                continue;
            }
            try {
                int value = Integer.parseInt(input);
                if (value > min) {
                    return value;
                } else {
                    outputManager.println("Value must be greater than " + min);
                    if (!scriptLines.isEmpty()) {
                        return null;
                    }
                }
            } catch (NumberFormatException e) {
                outputManager.println("Invalid number format. Please enter an integer.");
                if (!scriptLines.isEmpty()) {
                    return null;
                }
            }
        }
    }

    public Double getDoubleInput(String prompt) {
        while (true) {
            String input = readLine(prompt);
            if (input == null || input.trim().isEmpty()) {
                outputManager.println("This field cannot be empty.");
                if (!scriptLines.isEmpty()) {
                    return null;
                }
                continue;
            }
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                outputManager.println("Invalid number format. Please enter a double.");
                if (!scriptLines.isEmpty()) {
                    return null;
                }
            }
        }
    }

    public Long getLongInput(String prompt, boolean nullable, long min) {
        while (true) {
            String input = readLine(prompt);
            if (nullable && (input == null || input.trim().isEmpty() || Objects.equals(input, "null"))) {
                return null;
            }
            if (input == null || input.trim().isEmpty()) {
                outputManager.println("This field cannot be empty.");
                if (!scriptLines.isEmpty()) {
                    return null;
                }
                continue;
            }
            try {
                long value = Long.parseLong(input);
                if (value > min) {
                    return value;
                } else {
                    outputManager.println("Value must be greater than " + min);
                    if (!scriptLines.isEmpty()) {
                        return null;
                    }
                }
            } catch (NumberFormatException e) {
                outputManager.println("Invalid number format. Please enter a long integer.");
                if (!scriptLines.isEmpty()) {
                    return null;
                }
            }
        }
    }

    public ZonedDateTime getZonedDateTimeInput(String prompt) {
        while (true) {
            String input = readLine(prompt);
            if (input == null || input.trim().isEmpty()) {
                outputManager.println("This field cannot be empty.");
                if (!scriptLines.isEmpty()) {
                    return null;
                }
                continue;
            }
            try {
                return ZonedDateTime.parse(input);
            } catch (DateTimeParseException e) {
                outputManager.println("Invalid date/time format. Please enter a valid ZonedDateTime (e.g., 2023-10-27T10:00:00+00:00).");
                if (!scriptLines.isEmpty()) {
                    return null;
                }
            }
        }
    }

    public <T extends Enum<T>> T getEnumInput(String prompt, Class<T> enumClass, boolean nullable) {
        outputManager.println("Available options: " + String.join(", ", Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toArray(String[]::new)));
        while (true) {
            String input = readLine(prompt);
            if (nullable && (input == null || input.trim().isEmpty() || Objects.equals(input, "null"))) {
                return null;
            }
            if (input == null || input.trim().isEmpty()) {
                outputManager.println("This field cannot be empty.");
                if (!scriptLines.isEmpty()) {
                    return null;
                }
                continue;
            }
            try {
                return Enum.valueOf(enumClass, input.toUpperCase());
            } catch (IllegalArgumentException e) {
                outputManager.println("Invalid input. Please choose from: " + String.join(", ", Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toArray(String[]::new)));
                if (!scriptLines.isEmpty()) {
                    return null;
                }
            }
        }
    }

    public LabWork getLabWorkFromInput(User currentUser) {
        outputManager.println("Creating a new LabWork...");
        try {
            String name = getStringInput("Enter name: ", false);
            if (name == null) {
                outputManager.println("Failed to create LabWork: name is required.");
                return null;
            }

            Coordinates coordinates = getCoordinatesFromInput();
            if (coordinates == null) {
                outputManager.println("Failed to create LabWork: invalid coordinates.");
                return null;
            }

            Long minimalPoint = getLongInput("Enter minimalPoint (can be null, > 0): ", true, 0);
            if (minimalPoint == null && !scriptLines.isEmpty()) {
                outputManager.println("Failed to create LabWork: invalid minimalPoint in script.");
                return null;
            }

            Difficulty difficulty = getEnumInput("Enter difficulty: ", Difficulty.class, false);
            if (difficulty == null) {
                outputManager.println("Failed to create LabWork: difficulty is required.");
                return null;
            }

            Person author = getPersonFromInput();
            if (author == null) {
                outputManager.println("Failed to create LabWork: invalid author.");
                return null;
            }

            return new LabWork(0L, name, coordinates, ZonedDateTime.now(), minimalPoint, difficulty, author, currentUser);
        } catch (Exception e) {
            outputManager.println("Error creating LabWork: " + e.getMessage());
            return null;
        }
    }

    public Coordinates getCoordinatesFromInput() {
        outputManager.println("Creating Coordinates...");
        try {
            Double x = getDoubleInput("Enter x coordinate: ");
            if (x == null) {
                outputManager.println("Failed to create Coordinates: x coordinate is required.");
                return null;
            }

            Integer y = getIntInput("Enter y coordinate (must be > -563): ", -563);
            if (y == null) {
                outputManager.println("Failed to create Coordinates: invalid y coordinate.");
                return null;
            }

            return new Coordinates(x, y);
        } catch (Exception e) {
            outputManager.println("Error creating Coordinates: " + e.getMessage());
            return null;
        }
    }

    public Person getPersonFromInput() {
        outputManager.println("Creating Person...");
        try {
            String name = getStringInput("Enter person's name: ", false);
            if (name == null) {
                outputManager.println("Failed to create Person: name is required.");
                return null;
            }

            ZonedDateTime birthday = getZonedDateTimeInput("Enter birthday (ISO 8601 format): ");
            if (birthday == null) {
                outputManager.println("Failed to create Person: birthday is required.");
                return null;
            }

            String passportID = getStringInput("Enter passport ID: ", false);
            if (passportID == null) {
                outputManager.println("Failed to create Person: passport ID is required.");
                return null;
            }

            Colour eyeColour = getEnumInput("Enter eye Colour: ", Colour.class, false);
            if (eyeColour == null) {
                outputManager.println("Failed to create Person: eye Colour is required.");
                return null;
            }

            Colour hairColour = getEnumInput("Enter hair Colour (can be null): ", Colour.class, true);

            return new Person(0, name, birthday, passportID, eyeColour, hairColour);
        } catch (Exception e) {
            outputManager.println("Error creating Person: " + e.getMessage());
            return null;
        }
    }
}