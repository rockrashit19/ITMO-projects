from typing import Tuple, Dict, Any, List, Optional

class Cursor:
    def __init__(self, s: str):
        self.s = s
        self.i = 0
        self.n = len(s)

    def peek(self) -> str:
        return self.s[self.i] if self.i < self.n else ""

    def get(self) -> str:
        ch = self.peek()
        if ch:
            self.i += 1
        return ch

    def skip_ws(self):
        while self.i < self.n and self.s[self.i].isspace():
            self.i += 1

    def startswith(self, text: str) -> bool:
        return self.s.startswith(text, self.i)

def parse_name(cur: Cursor) -> str:
    name_chars = []
    ch = cur.peek()
    if not ch:
        return ""
    while cur.i < cur.n:
        ch = cur.peek()
        if ch.isalnum() or ch in "_-:.":
            name_chars.append(ch)
            cur.get()
        else:
            break
    return "".join(name_chars)

def parse_attr_value(cur: Cursor) -> str:
    cur.skip_ws()
    quote = cur.get()
    val_chars = []
    while cur.i < cur.n and cur.peek() != quote:
        val_chars.append(cur.get())
    if cur.peek() == quote:
        cur.get()
    return "".join(val_chars)

def parse_attrs(cur: Cursor) -> Dict[str, str]:
    attrs: Dict[str, str] = {}
    while True:
        cur.skip_ws()
        ch = cur.peek()
        if not ch or ch in ">/":
            break
        name = parse_name(cur)
        cur.skip_ws()
        if cur.peek() == "=":
            cur.get()
            cur.skip_ws()
            if cur.peek() in "\"'":
                val = parse_attr_value(cur)
                attrs[name] = val
            else:
                val_buf = []
                while cur.i < cur.n and (not cur.peek().isspace()) and cur.peek() not in ">/" :
                    val_buf.append(cur.get())
                attrs[name] = "".join(val_buf)
        else:
            attrs[name] = ""
    return attrs

def parse_text(cur: Cursor) -> str:
    buf = []
    while cur.i < cur.n and cur.peek() != "<":
        buf.append(cur.get())
    return "".join(buf)

def parse_element(cur: Cursor) -> Tuple[str, Dict[str, Any]]:
    cur.skip_ws()
    if cur.get() != "<":
        return "", {}
    tag = parse_name(cur)
    attrs = parse_attrs(cur)
    if cur.peek() == "/":
        cur.get()
        if cur.peek() == ">":
            cur.get()
        node: Dict[str, Any] = {}
        if attrs:
            node["@attrs"] = attrs
        return tag, node
    if cur.peek() == ">":
        cur.get()
    node: Dict[str, Any] = {}
    if attrs:
        node["@attrs"] = attrs

    children_by_tag: Dict[str, List[Any]] = {}
    text_accum: List[str] = []

    while cur.i < cur.n:
        if cur.startswith("</"):
            cur.get(); cur.get() 
            end_name = parse_name(cur)
            if cur.peek() == ">":
                cur.get()
            text_str = "".join(text_accum).strip()
            if text_str:
                if node:
                    node["#text"] = text_str
                else:
                    return tag, text_str # type: ignore
            for child_tag, items in children_by_tag.items():
                node[child_tag] = items[0] if len(items) == 1 else items
            return tag, node

        if cur.peek() == "<":
            ctag, cnode = parse_element(cur)
            if ctag:
                children_by_tag.setdefault(ctag, []).append(cnode)
        else:
            text_accum.append(parse_text(cur))

    text_str = "".join(text_accum).strip()
    if text_str:
        if node:
            node["#text"] = text_str
        else:
            return tag, text_str # type: ignore
    for child_tag, items in children_by_tag.items():
        node[child_tag] = items[0] if len(items) == 1 else items
    return tag, node

def xml_to_obj(xml_text: str) -> Dict[str, Any]:
    cur = Cursor(xml_text)
    tag, node = parse_element(cur)
    return {tag: node} if tag else {}

def dumps_json(obj: Any, indent: int = 2, ensure_ascii: bool = False, level: int = 0) -> str:
    sp = " " * (indent * level)

    def esc(s: str) -> str:
        out = []
        for ch in s:
            o = ord(ch)
            if ch == '"': out.append('\\"')
            elif ch == '\\': out.append('\\\\')
            elif ch == '\b': out.append('\\b')
            elif ch == '\f': out.append('\\f')
            elif ch == '\n': out.append('\\n')
            elif ch == '\r': out.append('\\r')
            elif ch == '\t': out.append('\\t')
            elif o < 0x20 or (ensure_ascii and o > 0x7F):
                out.append('\\u%04x' % o)
            else:
                out.append(ch)
        return '"' + "".join(out) + '"'

    if obj is None: return "null"
    if isinstance(obj, bool): return "true" if obj else "false"
    if isinstance(obj, (int, float)): return str(obj)
    if isinstance(obj, str): return esc(obj)
    if isinstance(obj, list):
        if not obj: return "[]"
        items = [dumps_json(x, indent, ensure_ascii, level + 1) for x in obj]
        return "[\n" + ",\n".join(" " * (indent*(level+1)) + it for it in items) + "\n" + sp + "]"
    if isinstance(obj, dict):
        if not obj: return "{}"
        items = []
        for k, v in obj.items():
            items.append(esc(str(k)) + ": " + dumps_json(v, indent, ensure_ascii, level + 1))
        return "{\n" + ",\n".join(" " * (indent*(level+1)) + it for it in items) + "\n" + sp + "}"
    return esc(str(obj))

if __name__ == "__main__":
    with open("data/input.xml", "r", encoding="utf-8") as f:
        xml_text = f.read()
    obj = xml_to_obj(xml_text)
    out = dumps_json(obj, indent=2, ensure_ascii=False)
    with open("data/output.json", "w", encoding="utf-8") as f:
        f.write(out)
