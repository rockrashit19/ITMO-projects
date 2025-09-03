from pathlib import Path
import json
from lark import Lark, Transformer, v_args

XML_GRAMMAR = r"""
?start: document

?document: prolog? element
prolog : XMLDECL
XMLDECL : "<?xml" /[^?]/* "?>"

?element : start_tag content end_tag
        | empty_elem_tag

start_tag      : "<" NAME attr* ">"
end_tag        : "</" NAME ">"
empty_elem_tag : "<" NAME attr* "/>"

attr : NAME "=" STRING
?content : (TEXT | element)*

NAME   : /[A-Za-z_][\w\-.:]*/
STRING : ESCAPED_STRING
TEXT   : /[^<]+/

%import common.ESCAPED_STRING
%import common.WS
%ignore WS
"""

@v_args(inline=True)
class XmlToJson(Transformer):
    def XMLDECL(self, tok):  
        return None

    def NAME(self, tok):
        return str(tok)

    def STRING(self, tok):
        s = str(tok)
        if len(s) >= 2 and s[0] == s[-1] and s[0] in ("'", '"'):
            s = s[1:-1]
        return s

    def attr(self, name, value):
        return (name, value)

    def start_tag(self, name, *attrs_items):
        attrs = {}
        for item in attrs_items:
            if isinstance(item, tuple) and len(item) == 2:
                k, v = item
                attrs[k] = v
        return ("START", name, attrs)

    def end_tag(self, name):
        return ("END", name)

    def empty_elem_tag(self, name, *attrs_items):
        attrs = {}
        for item in attrs_items:
            if isinstance(item, tuple) and len(item) == 2:
                k, v = item
                attrs[k] = v
        node = {}
        if attrs:
            node["@attrs"] = attrs
        return (name, node)

    def TEXT(self, tok):
        return str(tok)

    def content(self, *parts):
        children = {}
        texts = []
        for p in parts:
            if isinstance(p, tuple) and len(p) == 2 and p[0] == "#TEXT":
                texts.append(p[1])
            elif isinstance(p, tuple) and len(p) == 2 and isinstance(p[0], str):
                name, node = p
                children.setdefault(name, []).append(node)

        node = {}
        for k, arr in children.items():
            node[k] = arr[0] if len(arr) == 1 else arr

        text_value = "".join(texts).strip()
        if text_value:
            if node:
                node["#text"] = text_value
            else:
                return ("#ONLY_TEXT", text_value)
        return node

    def element(self, *parts):
        if len(parts) == 1 and isinstance(parts[0], tuple) and parts[0][0] not in ("START", "END"):
            return parts[0]

        start, *mid, end = parts # type: ignore
        _, name, attrs = start

        children = {}
        texts = []
        for it in mid:
            if isinstance(it, tuple) and len(it) == 2 and isinstance(it[0], str) and it[0] not in ("START", "END"):
                cname, cnode = it
                children.setdefault(cname, []).append(cnode)
            elif isinstance(it, str):
                texts.append(it)
        
        node = {k: (v[0] if len(v) == 1 else v) for k, v in children.items()}
        text_value = "".join(texts).strip()

        if text_value:
            if node:
                node["#text"] = text_value
            else:
                node = text_value 


        if attrs:
            if isinstance(node, dict):
                node = {"@attrs": attrs, **node} if node else {"@attrs": attrs}
            else:  
                node = {"@attrs": attrs, "#text": node}

        return (name, node)

    def document(self, *parts):
        for p in parts:
            if isinstance(p, tuple) and len(p) == 2 and isinstance(p[0], str):
                name, node = p
                return {name: node}
        return {}

def xml_to_json_file(in_path: str, out_path: str, indent: int = 2, ensure_ascii: bool = False):
    parser = Lark(XML_GRAMMAR, start="start", parser="lalr")
    text = Path(in_path).read_text(encoding="utf-8")
    tree = parser.parse(text)
    obj = XmlToJson().transform(tree)
    Path(out_path).parent.mkdir(parents=True, exist_ok=True)
    with open(out_path, "w", encoding="utf-8") as f:
        json.dump(obj, f, ensure_ascii=ensure_ascii, indent=indent)

if __name__ == "__main__":
    xml_to_json_file("data/input.xml", "data/output_grammar.json", indent=2, ensure_ascii=False)
