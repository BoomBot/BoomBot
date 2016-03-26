package net.lomeli.boombot.update;

public class UpdateJson {
    private int major, minor, revision;
    private String downloadURL;
    private String[] changeLog;

    public UpdateJson(int major, int minor, int revision, String downloadURL, String... changes) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.downloadURL = downloadURL;
        this.changeLog = changes;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getRevision() {
        return revision;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public String[] getChangeLog() {
        return changeLog;
    }

    public String getVersion() {
        return major + "." + minor + "." + revision;
    }
}
