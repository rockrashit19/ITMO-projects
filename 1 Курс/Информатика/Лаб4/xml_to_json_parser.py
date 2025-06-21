def read_file(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        return f.readlines()

def parse_xml(lines):
    stack = []
    current_dict = {}
    root = current_dict
    current_tag = None
    content = ""
    
    for line in lines:
        line = line.strip()
        if not line or line.startswith('<?xml'):
            continue
        i = 0
        while i < len(line):
            if line[i] == '<':
                if line[i+1] == '/':
                    end = line.find('>', i)
                    tag = line[i+2:end]
                    if content.strip():
                        if current_tag in current_dict:
                            if isinstance(current_dict[current_tag], list):
                                current_dict[current_tag].append(content.strip())
                            else:
                                current_dict[current_tag] = [current_dict[current_tag], content.strip()]
                        else:
                            current_dict[current_tag] = content.strip()
                    content = ""
                    stack.pop()
                    if stack:
                        current_dict = stack[-1]
                    current_tag = None
                    i = end + 1
                else:
                    end = line.find('>', i)
                    tag_start = line[i+1:end]
                    tag_name = tag_start.split()[0]
                    attrs = {}
                    if ' ' in tag_start:
                        attr_str = tag_start[len(tag_name):].strip()
                        attr_parts = attr_str.split('=')
                        for j in range(0, len(attr_parts)-1, 2):
                            key = attr_parts[j].strip()
                            value = attr_parts[j+1].strip().strip('"')
                            attrs[key] = value
                    new_dict = {'@attributes': attrs} if attrs else {}
                    if tag_name in current_dict:
                        if not isinstance(current_dict[tag_name], list):
                            current_dict[tag_name] = [current_dict[tag_name]]
                        current_dict[tag_name].append(new_dict)
                    else:
                        if tag_name == 'day' and tag_name not in current_dict:
                            current_dict[tag_name] = [new_dict]
                        else:
                            current_dict[tag_name] = new_dict
                    stack.append(current_dict)
                    current_dict = new_dict
                    current_tag = tag_name
                    i = end + 1
            else:
                content += line[i]
                i += 1
    
    return root

def dict_to_json(data, indent=0):
    if isinstance(data, dict):
        if not data:
            return "{}"
        result = ["{"]
        for key, value in data.items():
            line = '  ' * indent + f'"{key}": '
            if isinstance(value, dict):
                line += dict_to_json(value, indent + 1)
            elif isinstance(value, list):
                line += '[' + ', '.join(dict_to_json(item, indent + 1) if isinstance(item, dict) else f'"{item}"' for item in value) + ']'
            else:
                line += f'"{value}"'
            result.append(line + ',')
        result[-1] = result[-1][:-1]
        result.append('  ' * (indent - 1) + "}")
        return '\n'.join(result)
    return str(data)

def convert_xml_to_json(input_file, output_file):
    lines = read_file(input_file)
    parsed_data = parse_xml(lines)
    json_str = dict_to_json(parsed_data)
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(json_str)

convert_xml_to_json('data/input.xml', 'data/output.json')