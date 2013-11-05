package fr.utc.lo23.sharutc.controler.command.account;

/**
 * implementation of export profile command
 * {@inheritDoc}
 */
public class ExportProfileCommandImpl implements ExportProfileCommand{
    
    private String mSrcFile = "";
    private String mDestFolder = "";

    /**
     * Constructor
     */
    public ExportProfileCommandImpl() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSrcFile() {
        return mSrcFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSrcFile(String srcFile) {
        mSrcFile = srcFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDestFolder() {
        return mDestFolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDestFolder(String destFolder) {
        mDestFolder = destFolder;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        //TODO FileService.getInstance()
        //exportProfile(mSrcPath, mDestPath);
    }
}
