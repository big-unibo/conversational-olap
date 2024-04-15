package it.unibo.conversational.database;

import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Locale;

public class Cube {
    public Cube() {
    }

    public Cube(String datamart, String facttable, String dbms, String ip, int port, String user, String pwd, boolean create, boolean importmembers) {
        this.datamart = datamart;
        this.facttable = facttable;
        this.metadata = datamart;
        this.dbms = dbms;
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
        this.create = create;
        this.importmembers = importmembers;
    }

    private String datamart;
    private String facttable;
    private String metadata;
    private String dbms;
    private String ip;
    private int port;
    private boolean create;
    private boolean importmembers;
    private String user;
    private String pwd;
    private List<String> synonyms;

    public String getDataMart() {
        return datamart;
    }

    public void setDataMart(final String data_mart) {
        this.datamart = data_mart;
    }

    public String getFactTable() {
        return facttable.toLowerCase();
    }

    public void setFactTable(final String fact_table) {
        this.facttable = fact_table;
    }

    public String getMetaData() {
        return metadata;
    }

    public void setMetadata(final String metadata) {
        this.metadata = metadata;
    }

    public String getDbms() {
        return dbms;
    }

    public void setDbms(final String dbms) {
        this.dbms = dbms;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public boolean getCreate() {
        return create;
    }

    public void setCreate(final boolean create) {
        this.create = create;
    }

    public boolean getImportMembers() {
        return importmembers;
    }

    public void setImportmembers(final boolean importmembers) {
        this.importmembers = importmembers;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public String getSchema() {
        // THIS IS NECESSARY TO FIND THE PROPER TABLES
        if (getDbms().equalsIgnoreCase("oracle")) {
            return getUser().toUpperCase();
        } else {
            throw new NotImplementedException();
        }
    }
}
