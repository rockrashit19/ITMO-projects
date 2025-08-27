import re
import json


class RegexXmlParser:
    def __init__(self, xml_str: str):
        self.xml_doc = xml_str
        self.parsed = self._find_sub(self.xml_doc)

    def _find_sub(self, text: str) -> dict:
        tag, attrs, inner, rest = self._match_tag(text)
        if not tag:
            return {}

        node = {}
        if attrs:
            node["@attrs"] = attrs

        inner_has = self._has_tags(inner)
        rest_has = self._has_tags(rest)

        if not inner_has and not rest_has:
            node["#text"] = inner.strip()
            return {tag: node} if attrs else {tag: inner.strip()}

        if not inner_has and rest_has:
            if self._get_tag(rest) == tag:
                return {tag: self._get_list(text)}
            node["#text"] = inner.strip()
            return {tag: node} | self._find_sub(rest)

        if inner_has and not rest_has:
            node |= self._find_sub(inner)
            return {tag: node}

        if inner_has and rest_has:
            if self._get_tag(rest) == tag:
                return {tag: [self._find_sub(inner), self._find_sub(rest)]}
            node |= self._find_sub(inner)
            return {tag: node} | self._find_sub(rest)

        return {}

    def _get_list(self, text: str):
        matches = re.findall(r"<(\w+)>(.*?)</\1>", text, flags=re.S)
        return [m[1].strip() for m in matches]

    @staticmethod
    def _has_tags(text: str) -> bool:
        return bool(re.search(r"<\w+[^>]*>.*?</\w+>", text, flags=re.S))

    @staticmethod
    def _get_tag(text: str):
        m = re.search(r"<(\w+)[^>]*>", text)
        return m.group(1) if m else None

    @staticmethod
    def _match_tag(text: str):
        m = re.search(
            r"<(?P<tag>\w+)(?P<attrs>[^>]*)>(?P<inner>.*?)</\1>\s*(?P<rest>.*)",
            text,
            flags=re.S,
        )
        if not m:
            return "", {}, "", ""
        tag = m.group("tag")
        inner = m.group("inner")
        rest = m.group("rest")
        attrs_str = m.group("attrs").strip()

        attrs = {}
        if attrs_str:
            for a, v in re.findall(r'(\w+)="(.*?)"', attrs_str):
                attrs[a] = v

        return tag, attrs, inner, rest


if __name__ == "__main__":
    with open("data/input.xml", "r", encoding="utf-8") as f:
        parser = RegexXmlParser(f.read())

    with open("data/output_regex.json", "w", encoding="utf-8") as f:
        json.dump(parser.parsed, f, ensure_ascii=False, indent=2)
