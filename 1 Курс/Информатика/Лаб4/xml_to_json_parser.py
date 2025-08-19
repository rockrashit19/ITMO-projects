import re
import json


class XmlParser:

    def __init__(self, xml_str: str):
        self.xml_doc = xml_str
        self.__del_info_data()
        self.parsed = self.__find_sub(self.xml_doc)

    def __find_sub(self, line: str) -> dict:
        tag, attrs, info, rest = self.__match_tag(line)
        info_has_tags, rest_has_tags = self.__has_tags(info), self.__has_tags(rest)

        node = {}
        if attrs:  
            node["@attrs"] = attrs

        if not info_has_tags and not rest_has_tags:
            node["#text"] = info.strip()
            return {tag: node} if attrs else {tag: info.strip()}

        if not info_has_tags and rest_has_tags:
            if self.__get_tag(rest) == tag:
                return {tag: self.__get_list(line)}
            node["#text"] = info.strip()
            return {tag: node} | self.__find_sub(rest)

        if info_has_tags and not rest_has_tags:
            node |= self.__find_sub(info)
            return {tag: node}

        if info_has_tags and rest_has_tags:
            if self.__get_tag(rest) == tag:
                return {tag: [self.__find_sub(info), self.__find_sub(rest)]}
            node |= self.__find_sub(info)
            return {tag: node} | self.__find_sub(rest)
        
        return {}

    def __get_list(self, lines):
        infos = re.findall(r'<(\b\w+\b[\w ]*)(?:\s+[^>]*)?>(.*?)</\1>', lines, flags=re.S)
        return [i[1].strip() for i in infos]

    @staticmethod
    def __has_tags(lines):
        matched = re.search(r'<(\b\w+\b)[^>]*>.*?</\1>', lines, flags=re.S)
        return bool(matched)

    @staticmethod
    def __get_tag(lines):
        match = re.search(r'<(\b\w+\b)[^>]*>', lines, flags=re.S)
        return match.group(1) if match else None

    @staticmethod
    def __match_tag(lines):
        matched = re.search(
            r'<(?P<tag>\b\w+\b)(?P<attrs>[^>]*)>\s?(?P<info>.*?)</\1>\s?(?P<rest>.*)',
            lines,
            flags=re.S
        )
        if not matched:
            return "", {}, "", ""
        
        tag = matched.group("tag")
        attrs_str = matched.group("attrs").strip()
        info = re.sub(r'^ {0,4}\t?', '', matched.group("info"), flags=re.M)
        rest = matched.group("rest")

        attrs = {}
        if attrs_str:
            for pair in re.findall(r'(\w+)="(.*?)"', attrs_str):
                attrs[pair[0]] = pair[1]

        return tag, attrs, info, rest

    def __del_info_data(self):
        self.xml_doc = re.sub(r'<\?.*?\?>\s*', '', self.xml_doc)


if __name__ == '__main__':
    with open("data/input.xml", "r", encoding="utf-8") as input_file:
        parser = XmlParser(input_file.read())

    with open("data/output.json", "w", encoding="utf-8") as output_file:
        json.dump(parser.parsed, output_file, ensure_ascii=False, indent=4)
