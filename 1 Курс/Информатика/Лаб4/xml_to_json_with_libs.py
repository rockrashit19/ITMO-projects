import json
import xmltodict
from pathlib import Path

def xml_to_json_file(xml_path: str, json_path: str, ensure_ascii=False, indent=2):
    with open(xml_path, "r", encoding="utf-8") as f:
        data = xmltodict.parse(f.read())

    Path(json_path).parent.mkdir(parents=True, exist_ok=True)
    with open(json_path, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=ensure_ascii, indent=indent)

if __name__ == "__main__":
    xml_to_json_file("data/input.xml", "data/output_lib.json", ensure_ascii=False, indent=2)
