package commands;

public class SaveCommand implements XMLCommand {
    private XMLCommandHandler handler;

    public SaveCommand(XMLCommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute() throws Exception {
        handler.save();
    }
    
    @Override
    public String getHelp() {
        return "save                     - Save current XML structure to last used file";
    }
} 