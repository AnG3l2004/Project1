# Simple XML Parser

A basic XML parser implementation in Java without using any XML libraries.

## Current Project Structure

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

Under Development