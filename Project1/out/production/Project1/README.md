# XML Parser and Processor

A custom XML parser and processor implementation that supports basic XML operations and simplified XPath queries.

## Features

- XML file reading and parsing
- Element identification and unique ID management
- Basic XML operations (print, select, set, etc.)
- Simple XPath 2.0 query support
- Tree-based XML structure representation

## Supported Operations

1. `print` - Print formatted XML
2. `select <id> <key>` - Get attribute value
3. `set <id> <key> <value>` - Set attribute value
4. `children <id>` - List child elements
5. `child <id> <n>` - Access nth child
6. `text <id>` - Get element text content
7. `delete <id> <key>` - Delete attribute
8. `newchild <id>` - Create new child element
9. `xpath <id> <XPath>` - Execute XPath queries

## Project Structure

```
src/
├── core/
│   ├── xml_element.py       # Base XML element class
│   ├── xml_attribute.py     # XML attribute handling
│   └── xml_parser.py        # XML parsing logic
├── operations/
│   ├── basic_operations.py  # Basic XML operations
│   └── xpath_processor.py   # XPath processing
├── utils/
│   ├── id_generator.py      # Unique ID generation
│   └── xml_formatter.py     # XML formatting utilities
└── main.py                  # src.Main program entry point

tests/
├── test_data/              # XML test files
└── unit_tests/            # Test cases
```

## Getting Started

1. Clone the repository
2. Run `python src/main.py`
3. Use the available commands to interact with XML files

## Requirements

- Python 3.8+
- No external XML libraries required

## Development Status

�� Under Development