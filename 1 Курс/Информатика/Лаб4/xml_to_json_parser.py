def read_file(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        return f.readlines()
    
def parse_xml(lines):
    stack = []
    current_dict = []
    root = current_dict
    current_tag = None
    content = ""
    
    for line in lines:
        line = line.strip()
        if not line or line.startswith('<?xml'):
            continue
        i = 0
        
    
def dict_to_json(data, indent=0):
    if isinstance(data, dict):
        if not data:
            return "{}"
        result = ["{"]
        for key, value in data.items():
            line = '  ' * indent + f'"{key}": '
            if isinstance(value, dict):
                line += dict_to_json(value, indent = 1)
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