package banking.service;

import banking.model.Transaction;
import banking.model.TransactionType;
import banking.model.UserAccount;
import banking.security.PasswordUtil;
import banking.store.BankDataStore;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class BankService {
    private final BankDataStore dataStore;
    private final Map<String, UserAccount> accounts;
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^UGR/\\d{5}/\\d{2}$");

    public BankService(BankDataStore dataStore) {
        this.dataStore = dataStore;
        this.accounts = dataStore.loadAccounts();
        seedDemoAccountsIfEmpty();
    }

    private void seedDemoAccountsIfEmpty() {
        if (!accounts.isEmpty()) {
            return;
        }
        register("UGR/00001/24", "Admin", "User", "1234", 5000);
        register("UGR/00002/24", "Student", "One", "1111", 2800);
        register("UGR/00003/24", "Student", "Two", "2222", 3200);
    }

    public synchronized boolean register(String studentId, String firstName, String lastName, String password, double initialBalance) {
        String cleaned = normalizeStudentId(studentId);
        String cleanFirstName = cleanName(firstName);
        String cleanLastName = cleanName(lastName);
        if (!isValidStudentId(cleaned) || cleanFirstName.isEmpty() || cleanLastName.isEmpty() || password == null || password.length() < 4) {
            return false;
        }
        if (accounts.containsKey(cleaned)) {
            return false;
        }

        UserAccount account = new UserAccount(cleaned, cleanFirstName, cleanLastName, PasswordUtil.hash(password), initialBalance);
        account.addTransaction(new Transaction(
                TransactionType.DEPOSIT,
                initialBalance,
                "Initial account opening balance",
                initialBalance
        ));
        accounts.put(cleaned, account);
        persist();
        return true;
    }

    public synchronized UserAccount authenticate(String studentId, String password) {
        String cleaned = normalizeStudentId(studentId);
        UserAccount account = accounts.get(cleaned);
        if (account == null) {
            return null;
        }
        return PasswordUtil.matches(password, account.getPasswordHash()) ? account : null;
    }

    public synchronized String deposit(String studentId, double amount) {
        UserAccount account = accountOrNull(studentId);
        if (account == null || !isValidAmount(amount)) {
            return "Invalid deposit request.";
        }
        account.deposit(amount);
        account.addTransaction(new Transaction(TransactionType.DEPOSIT, amount, "Cash deposit", account.getBalance()));
        persist();
        return "Deposit successful.";
    }

    public synchronized String withdraw(String studentId, double amount) {
        UserAccount account = accountOrNull(studentId);
        if (account == null || !isValidAmount(amount)) {
            return "Invalid withdrawal request.";
        }
        if (amount > account.getBalance()) {
            return "Insufficient balance.";
        }
        account.withdraw(amount);
        account.addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount, "Cash withdrawal", account.getBalance()));
        persist();
        return "Withdrawal successful.";
    }

    public synchronized String transfer(String senderStudentId, String receiverStudentId, double amount) {
        if (!isValidAmount(amount)) {
            return "Amount must be greater than 0.";
        }

        String normalizedReceiver = normalizeStudentId(receiverStudentId);
        if (!isValidStudentId(normalizedReceiver)) {
            return "Invalid recipient ID. Use UGR/XXXXX/XX.";
        }

        UserAccount sender = accountOrNull(senderStudentId);
        UserAccount receiver = accountOrNull(normalizedReceiver);
        if (sender == null) {
            return "Sender account not found.";
        }
        if (receiver == null) {
            return "Receiver account not found.";
        }
        if (sender.getUsername().equals(receiver.getUsername())) {
            return "Cannot transfer to your own account.";
        }
        if (amount > sender.getBalance()) {
            return "Insufficient balance.";
        }

        sender.withdraw(amount);
        receiver.deposit(amount);
        sender.addTransaction(new Transaction(
                TransactionType.TRANSFER_OUT,
                amount,
                "Transfer to " + receiver.getUsername(),
                sender.getBalance()
        ));
        receiver.addTransaction(new Transaction(
                TransactionType.TRANSFER_IN,
                amount,
                "Transfer from " + sender.getUsername(),
                receiver.getBalance()
        ));
        persist();
        return "Transfer completed successfully.";
    }

    public synchronized boolean changePassword(String studentId, String oldPassword, String newPassword) {
        UserAccount account = accountOrNull(studentId);
        if (account == null || oldPassword == null || newPassword == null || newPassword.length() < 4) {
            return false;
        }
        if (!PasswordUtil.matches(oldPassword, account.getPasswordHash())) {
            return false;
        }
        account.setPasswordHash(PasswordUtil.hash(newPassword));
        persist();
        return true;
    }

    public synchronized double getBalance(String studentId) {
        UserAccount account = accountOrNull(studentId);
        return account == null ? 0 : account.getBalance();
    }

    public synchronized List<Transaction> getTransactions(String studentId) {
        UserAccount account = accountOrNull(studentId);
        if (account == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(account.getTransactions());
    }

    public synchronized List<String> getAllUsernames() {
        return new ArrayList<>(accounts.keySet());
    }

    public synchronized String getFirstName(String studentId) {
        UserAccount account = accountOrNull(studentId);
        if (account == null) {
            return "";
        }
        String firstName = account.getFirstName();
        if (firstName.isBlank()) {
            return account.getUsername();
        }
        return firstName;
    }

    public String normalizeStudentId(String studentId) {
        if (studentId == null) {
            return "";
        }
        return studentId.trim().toUpperCase();
    }

    public boolean isValidStudentId(String studentId) {
        return STUDENT_ID_PATTERN.matcher(studentId).matches();
    }

    private UserAccount accountOrNull(String studentId) {
        return accounts.get(normalizeStudentId(studentId));
    }

    private boolean isValidAmount(double amount) {
        return amount > 0.0;
    }

    private String cleanName(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(trimmed.charAt(0)) + trimmed.substring(1).toLowerCase();
    }

    private void persist() {
        dataStore.saveAccounts(accounts);
        savePerUserFiles();
    }

    private void savePerUserFiles() {
        File dir = new File("data/users");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (UserAccount account : accounts.values()) {
            String safeName = account.getUsername().replace('/', '_');
            File userFile = new File(dir, safeName + ".txt");
            try (PrintWriter out = new PrintWriter(userFile)) {
                out.println("studentId=" + account.getUsername());
                out.println("firstName=" + account.getFirstName());
                out.println("lastName=" + account.getLastName());
                out.println("balance=" + account.getBalance());
                out.println("transactions=" + account.getTransactions().size());
            } catch (IOException ignored) {
            }
        }
    }
}
