package banking.store;

import banking.model.UserAccount;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class BankDataStore {
    private final File dataFile;

    public BankDataStore(String filePath) {
        this.dataFile = new File(filePath);
    }

    @SuppressWarnings("unchecked")
    public Map<String, UserAccount> loadAccounts() {
        if (!dataFile.exists()) {
            return new LinkedHashMap<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile))) {
            Object obj = in.readObject();
            if (obj instanceof Map<?, ?> map) {
                return (Map<String, UserAccount>) map;
            }
            return new LinkedHashMap<>();
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
    }

    public void saveAccounts(Map<String, UserAccount> accounts) {
        File parent = dataFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            out.writeObject(accounts);
        } catch (IOException ignored) {
        }
    }
}
