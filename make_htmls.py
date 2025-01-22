from pathlib import Path
from markdown import markdown

TEMPLATE = """
<!DOCTYPE html>
<html>
<head>
<title>leuchtstark.dev - {title:s}</title>
</head>
<body>
{body:s}
</body>
</html>
"""

results_path = "build"
Path(results_path).mkdir(parents=True, exist_ok=True)

markdown_files = {
    "privacy.md": "Privacy Policy"
}

for filename, title in markdown_files.items():
    filename_html = filename.replace(".md",".html")

    text_markdown = Path(filename).read_text()

    text_html = TEMPLATE.format(
        title=title,
        body = markdown(text_markdown)
    )

    Path(results_path).joinpath(filename_html).write_text(text_html)

