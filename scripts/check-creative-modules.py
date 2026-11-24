#!/usr/bin/env python3
"""Check publication/module edges and framework-free domain boundaries."""
from pathlib import Path
import re
ROOT=Path(__file__).resolve().parents[1]
modules={p.parent.name:p for p in ROOT.glob("core/creative-*/build.gradle.kts")}
assert len(modules)==25, f"Expected 25 creative modules, found {len(modules)}"
graph={}
for name,build in modules.items():
    text=build.read_text()
    assert 'id("noel.libs.android.library")' in text, name
    assert f'module("core/{name}")' in (ROOT/"settings.gradle.kts").read_text(),name
    production="\n".join(line for line in text.splitlines() if not line.strip().startswith(("testImplementation", "androidTestImplementation")))
    graph[name]=re.findall(r'project\(":core-(creative-[^"\)]+)"\)',production)
    if name.endswith("-engine") or name in {"creative-model","creative-renderer"}:
        assert 'android.compose' not in text and 'libs.androidx.compose' not in text,name
    domain_files=list(build.parent.glob("src/main/kotlin/**/domain/*.kt"))
    if name=="creative-model": domain_files+=list(build.parent.glob("src/main/kotlin/**/*.kt"))
    for file in domain_files:
        for line in file.read_text().splitlines():
            assert not re.match(r"import (android\.|androidx\.|.*\.(data|presentation)\.)",line), f"{file.name}: {line}"
    for file in build.parent.glob("src/main/kotlin/**/presentation/*.kt"):
        assert not re.search(r"^import .*\.data\.",file.read_text(),re.M),file

def walk(name,active=()):
    assert name not in active,f"Cycle: {active} -> {name}"
    for child in graph[name]:
        assert child in modules,f"Unknown module: {child}"
        if name.endswith("-engine"):
            assert not child.endswith("-compose") and child!="creative-compose-common",name
            assert not child.endswith("-engine"),f"Engine implementation coupling: {name} -> {child}"
        walk(child,active+(name,))
for name in modules: walk(name)
print("PASS: 25 creative artifacts; acyclic graph; engine/UI separation; domain and presentation imports")
