import java.io.*;

public class XMLParser {
    private XMLElement root;
    private String currentFile;

    public void open(String filename) throws IOException {
        currentFile = filename;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            root = new XMLElement("root");
        }
    }

    public void save() throws IOException {
        if (currentFile == null) throw new IOException("No file is open");
        try (PrintWriter writer = new PrintWriter(currentFile)) {
            writer.println("<root></root>");
        }
    }

    public XMLElement getRoot() { return root; }
} 
