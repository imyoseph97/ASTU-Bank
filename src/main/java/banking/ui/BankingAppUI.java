package banking.ui;

import banking.model.Transaction;
import banking.service.BankService;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BankingAppUI {
    private final BankService bankService;
    private JFrame loginFrame;
    private JFrame dashboardFrame;
    private String currentUsername;
    private String currentFirstName;

    private JLabel balanceLabel;
    private DefaultTableModel tableModel;
    private JLabel statCurrentLabel;
    private JLabel statAvailableLabel;

    private static final Color PRIMARY = new Color(0, 102, 255);
    private static final Color DARK_BLUE = new Color(0, 69, 173);
    private static final Color BG = new Color(245, 248, 253);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 30);
    private static final Font HEADING_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public BankingAppUI(BankService bankService) {
        this.bankService = bankService;
    }

    public void show() {
        SwingUtilities.invokeLater(this::buildLoginFrame);
    }

    private void buildLoginFrame() {
        loginFrame = new JFrame("ASTU Bank");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(920, 760);
        loginFrame.setLocationRelativeTo(null);

        LoginScenePanel root = new LoginScenePanel();
        root.setLayout(new BorderLayout(8, 0));
        root.setBorder(new EmptyBorder(28, 36, 28, 36));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBorder(new EmptyBorder(32, 4, 32, 20));
        brand.setPreferredSize(new Dimension(300, 0));

        JLabel wordTop = new JLabel("ASTU");
        wordTop.setFont(new Font("Segoe UI", Font.BOLD, 58));
        wordTop.setForeground(new Color(255, 255, 255));
        wordTop.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel wordSub = new JLabel("Bank");
        wordSub.setFont(new Font("Segoe UI", Font.PLAIN, 34));
        wordSub.setForeground(new Color(186, 224, 255));
        wordSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel hook = new JLabel("<html><div style='width:240px'>Banking that keeps pace with<br/>deadlines, exams, and campus life.</div></html>");
        hook.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        hook.setForeground(new Color(210, 230, 250));
        hook.setAlignmentX(Component.LEFT_ALIGNMENT);

        brand.add(wordTop);
        brand.add(Box.createVerticalStrut(2));
        brand.add(wordSub);
        brand.add(Box.createVerticalStrut(22));
        brand.add(hook);
        brand.add(Box.createVerticalStrut(28));
        brand.add(heroBullet("Campus-ready ETB balances & transfers"));
        brand.add(Box.createVerticalStrut(10));
        brand.add(heroBullet("Sign-in shielded for student accounts"));
        brand.add(Box.createVerticalStrut(10));
        brand.add(heroBullet("A calm portal for hectic semesters"));
        brand.add(Box.createVerticalGlue());

        JPanel cardWrap = new JPanel(new GridBagLayout());
        cardWrap.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        int cardArc = 26;
        RoundedFillPanel card = new RoundedFillPanel(cardArc);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new CreativeBorder(new Color(11, 96, 229), new Color(71, 164, 255), cardArc),
                new EmptyBorder(20, 24, 20, 24)
        ));
        card.setPreferredSize(new Dimension(470, 520));

        JLabel title = new JLabel("ASTU Bank");
        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Log in to ASTU Bank");
        subtitle.setFont(BODY_FONT);
        subtitle.setForeground(new Color(60, 73, 95));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hero = new JLabel("Secure Student Banking Portal");
        hero.setAlignmentX(Component.CENTER_ALIGNMENT);
        hero.setFont(new Font("Segoe UI", Font.BOLD, 13));
        hero.setForeground(new Color(28, 80, 160));
        hero.setBorder(new CompoundBorder(
                new RoundedBorder(new Color(192, 214, 246), 16),
                new EmptyBorder(8, 14, 8, 14)
        ));

        JTextField userField = roundedTextField(22);
        userField.setToolTipText("UGR/XXXXX/XX");
        JPasswordField passField = roundedPasswordField(22);
        userField.setMaximumSize(new Dimension(360, 42));
        userField.setPreferredSize(new Dimension(360, 42));
        passField.setMaximumSize(new Dimension(360, 42));
        passField.setPreferredSize(new Dimension(360, 42));
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passField.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton loginButton = roundedButton("Log in", PRIMARY, Color.WHITE, 24);
        JButton registerButton = roundedButton("Create new account", Color.WHITE, PRIMARY, 24);
        registerButton.setBorder(new RoundedBorder(PRIMARY, 24));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(360, 44));
        registerButton.setMaximumSize(new Dimension(360, 44));
        loginButton.setHorizontalAlignment(SwingConstants.CENTER);
        registerButton.setHorizontalAlignment(SwingConstants.CENTER);

        loginButton.addActionListener(e -> {
            String studentId = bankService.normalizeStudentId(userField.getText());
            userField.setText(studentId);
            if (!bankService.isValidStudentId(studentId)) {
                JOptionPane.showMessageDialog(loginFrame, "Invalid User ID format. Use UGR/XXXXX/XX.");
                return;
            }
            String password = new String(passField.getPassword());
            if (bankService.authenticate(studentId, password) == null) {
                JOptionPane.showMessageDialog(loginFrame, "Invalid User ID or password.");
                return;
            }
            currentUsername = studentId;
            currentFirstName = bankService.getFirstName(studentId);
            loginFrame.dispose();
            buildDashboardFrame();
        });

        registerButton.addActionListener(e -> openRegisterDialog());

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(12));
        card.add(hero);
        card.add(Box.createVerticalStrut(20));
        JLabel userIdLabel = label("User ID");
        userIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userIdLabel.setHorizontalAlignment(SwingConstants.LEFT);
        userIdLabel.setMaximumSize(new Dimension(360, 20));
        userIdLabel.setPreferredSize(new Dimension(360, 20));
        card.add(userIdLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(userField);
        card.add(Box.createVerticalStrut(14));
        JLabel passwordLabel = label("Password");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);
        passwordLabel.setMaximumSize(new Dimension(360, 20));
        passwordLabel.setPreferredSize(new Dimension(360, 20));
        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(passField);

        JLabel forgot = new JLabel("Forgot password?");
        forgot.setFont(new Font("Segoe UI", Font.BOLD, 13));
        forgot.setForeground(new Color(35, 61, 115));
        forgot.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgot.setHorizontalAlignment(SwingConstants.LEFT);
        forgot.setMaximumSize(new Dimension(360, 20));
        forgot.setPreferredSize(new Dimension(360, 20));
        forgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                        loginFrame,
                        "If you forgot your password, please visit ASTU Bank in person and contact the support desk.\n" +
                                "Our team will verify your identity and help you reset your password safely.",
                        "Password Recovery Support",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        card.add(Box.createVerticalStrut(12));
        card.add(forgot);
        card.add(Box.createVerticalStrut(16));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(10));
        card.add(registerButton);

        cardWrap.add(card, gbc);
        root.add(brand, BorderLayout.WEST);
        root.add(cardWrap, BorderLayout.CENTER);
        loginFrame.setContentPane(root);
        loginFrame.setVisible(true);
    }

    private JLabel heroBullet(String text) {
        JLabel line = new JLabel("<html><div style='width:260px'><span style='color:#9ED0FF;'>&#9679;</span> <span style='color:#E8F2FF;'>" +
                text +
                "</span></div></html>");
        line.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        line.setAlignmentX(Component.LEFT_ALIGNMENT);
        return line;
    }

    private void openRegisterDialog() {
        JDialog dialog = new JDialog(loginFrame, "Register ASTU Student", true);
        dialog.setSize(500, 520);
        dialog.setLocationRelativeTo(loginFrame);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JTextField userField = roundedTextField(16);
        userField.setToolTipText("UGR/XXXXX/XX");
        JTextField firstNameField = roundedTextField(16);
        JTextField lastNameField = roundedTextField(16);
        JPasswordField passField = roundedPasswordField(16);
        JPasswordField confirmField = roundedPasswordField(16);
        JTextField depositField = roundedTextField(16);
        depositField.setText("500");

        JButton createButton = roundedButton("Create", PRIMARY, Color.WHITE, 16);
        createButton.addActionListener(e -> {
            String studentId = bankService.normalizeStudentId(userField.getText());
            userField.setText(studentId);
            if (!bankService.isValidStudentId(studentId)) {
                JOptionPane.showMessageDialog(dialog, "Invalid User ID format. Use UGR/XXXXX/XX.");
                return;
            }
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String pass = new String(passField.getPassword());
            String confirm = new String(confirmField.getPassword());
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(dialog, "Passwords do not match.");
                return;
            }
            double initialDeposit;
            try {
                initialDeposit = Double.parseDouble(depositField.getText().trim());
                if (initialDeposit < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid initial deposit amount.");
                return;
            }

            boolean ok = bankService.register(studentId, firstName, lastName, pass, initialDeposit);
            if (!ok) {
                JOptionPane.showMessageDialog(dialog, "Registration failed. Check User ID, first/last name, or password.");
                return;
            }

            JOptionPane.showMessageDialog(dialog, "Account created and saved successfully.");
            dialog.dispose();
        });

        panel.add(label("User ID (UGR/XXXXX/XX)"));
        panel.add(userField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(label("First Name"));
        panel.add(firstNameField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(label("Last Name"));
        panel.add(lastNameField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(label("Password (min 4 chars)"));
        panel.add(passField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(label("Confirm Password"));
        panel.add(confirmField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(label("Initial Deposit"));
        panel.add(depositField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(createButton);

        JScrollPane formScrollPane = new JScrollPane(panel);
        formScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        formScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.addTab("Register", formScrollPane);

        dialog.setContentPane(tabs);
        dialog.setVisible(true);
    }

    private void buildDashboardFrame() {
        // Ensure dashboard widgets are always bound to the current session's UI.
        resetDashboardBindings();
        dashboardFrame = new JFrame("ASTU Bank Dashboard - " + currentUsername);
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setSize(1120, 720);
        dashboardFrame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(buildTopPanel(), BorderLayout.NORTH);
        root.add(buildCenterPanel(), BorderLayout.CENTER);
        root.add(buildBottomPanel(), BorderLayout.SOUTH);

        dashboardFrame.setContentPane(root);
        refreshBalance();
        refreshTransactions();
        dashboardFrame.setVisible(true);
    }

    private JPanel buildTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(new EmptyBorder(10, 18, 10, 18));
        top.setBackground(Color.WHITE);

        String displayName = (currentFirstName == null || currentFirstName.isBlank()) ? currentUsername : currentFirstName;
        JLabel welcome = new JLabel("Welcome, " + displayName + "  |  ASTU Bank");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcome.setForeground(new Color(35, 53, 87));

        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        balanceLabel.setForeground(PRIMARY);

        top.add(welcome, BorderLayout.WEST);
        top.add(balanceLabel, BorderLayout.EAST);
        return top;
    }

    private JComponent buildCenterPanel() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.addTab("Dashboard", buildDashboardTab());
        tabs.addTab("Transfer", buildTransferPanel());
        tabs.addTab("Security", buildSecurityPanel());
        tabs.addTab("Transactions", buildTransactionsPanel());
        tabs.setBackground(Color.WHITE);
        return tabs;
    }

    private JPanel buildDashboardTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));
        panel.setBackground(BG);

        JPanel statRow = new JPanel(new GridLayout(1, 2, 12, 12));
        statRow.setOpaque(false);
        statRow.add(balanceCard("Student Account", "Current Balance", true));
        statRow.add(balanceCard("Available Funds", "Ready to use", false));

        JPanel quickRow = new JPanel(new GridLayout(1, 2, 12, 12));
        quickRow.setOpaque(false);
        quickRow.add(actionCard("Deposit", "Add money", true));
        quickRow.add(actionCard("Withdraw", "Withdraw money", false));

        panel.add(statRow, BorderLayout.NORTH);
        panel.add(quickRow, BorderLayout.CENTER);
        return panel;
    }

    private JPanel actionCard(String title, String subtitle, boolean deposit) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(214, 225, 242), 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));
        card.setBackground(Color.WHITE);

        JLabel header = new JLabel(title);
        header.setFont(new Font("Segoe UI", Font.BOLD, 19));
        header.setForeground(PRIMARY);
        JLabel desc = new JLabel(subtitle);
        desc.setFont(BODY_FONT);
        desc.setForeground(new Color(85, 100, 128));

        JTextField amountField = roundedTextField(14);
        amountField.setPreferredSize(new Dimension(230, 38));
        amountField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        JButton actionButton = roundedButton(title, PRIMARY, Color.WHITE, 18);

        actionButton.addActionListener(e -> {
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dashboardFrame, "Please enter a valid amount.");
                return;
            }

            if (!deposit) {
                JPanel confirm = new JPanel();
                confirm.setLayout(new BoxLayout(confirm, BoxLayout.Y_AXIS));
                confirm.add(new JLabel("Enter your password to confirm this withdrawal."));
                confirm.add(Box.createVerticalStrut(8));
                JPasswordField confirmPass = roundedPasswordField(14);
                confirmPass.setPreferredSize(new Dimension(220, 38));
                confirmPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
                confirm.add(confirmPass);

                int choice = JOptionPane.showConfirmDialog(
                        dashboardFrame,
                        confirm,
                        "Confirm withdrawal",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
                if (choice != JOptionPane.OK_OPTION) {
                    return;
                }
                String password = new String(confirmPass.getPassword());
                confirmPass.setText("");
                if (bankService.authenticate(currentUsername, password) == null) {
                    JOptionPane.showMessageDialog(dashboardFrame, "Incorrect password. Withdrawal was not processed.");
                    return;
                }
            }

            String result = deposit
                    ? bankService.deposit(currentUsername, amount)
                    : bankService.withdraw(currentUsername, amount);
            JOptionPane.showMessageDialog(dashboardFrame, result);
            refreshBalance();
            refreshTransactions();
            amountField.setText("");
        });

        card.add(header);
        card.add(Box.createVerticalStrut(6));
        card.add(desc);
        card.add(Box.createVerticalStrut(8));
        card.add(label("Amount (ETB)"));
        card.add(amountField);
        card.add(Box.createVerticalStrut(14));
        card.add(actionButton);
        return card;
    }

    private JPanel balanceCard(String title, String subtitle, boolean primaryCard) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(209, 224, 246), 1, true),
                new EmptyBorder(0, 0, 12, 0)
        ));
        card.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(primaryCard ? PRIMARY : DARK_BLUE);
        header.setBorder(new EmptyBorder(10, 12, 10, 12));
        JLabel hTitle = new JLabel(title);
        hTitle.setForeground(Color.WHITE);
        hTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.add(hTitle, BorderLayout.WEST);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(79, 96, 132));
        sub.setBorder(new EmptyBorder(10, 12, 2, 12));

        JLabel value = new JLabel("ETB 0.00");
        value.setFont(new Font("Segoe UI", Font.BOLD, 22));
        value.setForeground(new Color(32, 56, 111));
        value.setBorder(new EmptyBorder(2, 12, 6, 12));

        if (primaryCard) {
            statCurrentLabel = value;
        } else {
            statAvailableLabel = value;
        }

        card.add(header);
        card.add(sub);
        card.add(value);
        return card;
    }

    private JPanel buildTransferPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Transfer Money");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(23, 51, 106));
        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Send money to another ASTU student account."));
        panel.add(Box.createVerticalStrut(14));

        JTextField recipientField = roundedTextField(14);
        recipientField.setToolTipText("UGR/XXXXX/XX");
        recipientField.setMaximumSize(new Dimension(340, 38));
        recipientField.setPreferredSize(new Dimension(340, 38));
        JTextField amountField = roundedTextField(14);
        amountField.setMaximumSize(new Dimension(220, 38));
        amountField.setPreferredSize(new Dimension(220, 38));
        JButton transferButton = roundedButton("Transfer Now", PRIMARY, Color.WHITE, 18);

        transferButton.addActionListener(e -> {
            String target = bankService.normalizeStudentId(recipientField.getText());
            recipientField.setText(target);
            if (!bankService.isValidStudentId(target)) {
                JOptionPane.showMessageDialog(dashboardFrame, "Invalid recipient User ID format. Use UGR/XXXXX/XX.");
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dashboardFrame, "Invalid amount.");
                return;
            }
            String message = bankService.transfer(currentUsername, target, amount);
            JOptionPane.showMessageDialog(dashboardFrame, message);
            refreshBalance();
            refreshTransactions();
            amountField.setText("");
        });

        panel.add(label("Recipient User ID (UGR/XXXXX/XX)"));
        panel.add(recipientField);
        panel.add(Box.createVerticalStrut(8));
        panel.add(label("Amount"));
        panel.add(amountField);
        panel.add(Box.createVerticalStrut(14));
        panel.add(transferButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel buildSecurityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Security Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(23, 51, 106));
        panel.add(title);
        panel.add(Box.createVerticalStrut(12));

        JPasswordField oldPass = roundedPasswordField(14);
        JPasswordField newPass = roundedPasswordField(14);
        JPasswordField confirmPass = roundedPasswordField(14);
        JButton changeButton = roundedButton("Change Password", PRIMARY, Color.WHITE, 18);

        changeButton.addActionListener(e -> {
            String oldPassword = new String(oldPass.getPassword());
            String pass1 = new String(newPass.getPassword());
            String pass2 = new String(confirmPass.getPassword());
            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(dashboardFrame, "Passwords do not match.");
                return;
            }
            boolean ok = bankService.changePassword(currentUsername, oldPassword, pass1);
            JOptionPane.showMessageDialog(dashboardFrame, ok ? "Password changed." : "Password update failed. Old password may be wrong.");
            oldPass.setText("");
            newPass.setText("");
            confirmPass.setText("");
        });

        panel.add(label("Old Password"));
        panel.add(oldPass);
        panel.add(Box.createVerticalStrut(8));
        panel.add(label("New Password"));
        panel.add(newPass);
        panel.add(Box.createVerticalStrut(8));
        panel.add(label("Confirm Password"));
        panel.add(confirmPass);
        panel.add(Box.createVerticalStrut(14));
        panel.add(changeButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JScrollPane buildTransactionsPanel() {
        String[] columns = {"Time", "Type", "Amount", "Balance After", "Note"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(BODY_FONT);
        table.getTableHeader().setFont(LABEL_FONT);
        table.getTableHeader().setBackground(new Color(238, 246, 255));
        table.getTableHeader().setForeground(new Color(34, 57, 103));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new CompoundBorder(
                new EmptyBorder(16, 16, 16, 16),
                new LineBorder(new Color(211, 224, 245), 1, true)
        ));
        return scroll;
    }

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(6, 10, 10, 16));

        JButton logout = roundedButton("Logout", Color.WHITE, DARK_BLUE, 16);
        logout.setBorder(new RoundedBorder(new Color(189, 208, 238), 16));
        logout.addActionListener(e -> {
            dashboardFrame.dispose();
            currentUsername = null;
            currentFirstName = null;
            resetDashboardBindings();
            buildLoginFrame();
        });
        panel.add(logout);
        return panel;
    }

    private void resetDashboardBindings() {
        balanceLabel = null;
        statCurrentLabel = null;
        statAvailableLabel = null;
        tableModel = null;
    }

    private void refreshBalance() {
        double balance = bankService.getBalance(currentUsername);
        balanceLabel.setText(String.format("Balance: ETB %.2f", balance));
        if (statCurrentLabel != null) {
            statCurrentLabel.setText(String.format("ETB %.2f", balance));
        }
        if (statAvailableLabel != null) {
            statAvailableLabel.setText(String.format("ETB %.2f", balance));
        }
    }

    private void refreshTransactions() {
        if (tableModel == null) {
            return;
        }
        tableModel.setRowCount(0);
        List<Transaction> transactions = bankService.getTransactions(currentUsername);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Transaction tx : transactions) {
            tableModel.addRow(new Object[]{
                    tx.getTimestamp().format(formatter),
                    tx.getType(),
                    String.format("%.2f", tx.getAmount()),
                    String.format("%.2f", tx.getBalanceAfter()),
                    tx.getNote()
            });
        }
    }

    private JTextField roundedTextField(int radius) {
        JTextField field = new JTextField();
        field.setFont(BODY_FONT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setPreferredSize(new Dimension(280, 34));
        field.setBorder(new CompoundBorder(new RoundedBorder(new Color(186, 199, 220), radius), new EmptyBorder(8, 14, 8, 14)));
        return field;
    }

    private JPasswordField roundedPasswordField(int radius) {
        JPasswordField field = new JPasswordField();
        field.setFont(BODY_FONT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setPreferredSize(new Dimension(280, 34));
        field.setBorder(new CompoundBorder(new RoundedBorder(new Color(186, 199, 220), radius), new EmptyBorder(8, 14, 8, 14)));
        return field;
    }

    private JButton roundedButton(String text, Color bg, Color fg, int radius) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(10, 14, 10, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setUI(new RoundedButtonUI(radius, bg));
        return button;
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(new Color(41, 57, 89));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private static class RoundedBorder extends LineBorder {
        private final int radius;

        RoundedBorder(Color color, int radius) {
            super(color, 1, true);
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getLineColor());
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    private static class RoundedButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        private final int radius;
        private final Color background;

        RoundedButtonUI(int radius, Color background) {
            this.radius = radius;
            this.background = background;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fill(new RoundRectangle2D.Double(0, 0, c.getWidth(), c.getHeight(), radius, radius));
            g2.dispose();
            super.paint(g, c);
        }
    }

    /**
     * Paints the card background as a round-rect (not a full rectangle) and clips children
     * so opaque fields/buttons cannot draw past the rounded edge past the blue border.
     */
    private static class RoundedFillPanel extends JPanel {
        private final int arc;

        RoundedFillPanel(int arc) {
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
            g2.dispose();
        }

        @Override
        public void paintChildren(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.clip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arc, arc));
            super.paintChildren(g2);
            g2.dispose();
        }
    }

    private static class CreativeBorder extends LineBorder {
        private final Color accent;
        private final int radius;

        CreativeBorder(Color main, Color accent, int radius) {
            super(main, 2, true);
            this.accent = accent;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gp = new GradientPaint(0, 0, getLineColor(), width, height, accent);
            g2.setPaint(gp);
            g2.setStroke(new BasicStroke(2.4f));
            g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);

            g2.setColor(new Color(11, 96, 229, 36));
            g2.setStroke(new BasicStroke(6f));
            g2.drawRoundRect(x + 4, y + 4, width - 9, height - 9, radius - 6, radius - 6);
            g2.dispose();
        }
    }

    private static class LoginScenePanel extends JPanel {
        LoginScenePanel() {
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            if (w <= 0 || h <= 0) {
                g2.dispose();
                return;
            }

            float[] dist = {0f, 0.42f, 1f};
            Color[] colors = {
                    new Color(10, 36, 88),
                    new Color(0, 86, 198),
                    new Color(0, 178, 214)
            };
            LinearGradientPaint sky = new LinearGradientPaint(0, 0, w, (float) h * 0.92f, dist, colors);
            g2.setPaint(sky);
            g2.fillRect(0, 0, w, h);

            Composite saved = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.24f));
            g2.setColor(new Color(130, 210, 255));
            g2.fill(new Ellipse2D.Double(-w * 0.18, h * 0.08, w * 0.58, h * 0.48));

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            g2.setColor(new Color(0, 48, 120));
            g2.fill(new Ellipse2D.Double(w * 0.32, -h * 0.12, w * 0.75, h * 0.55));

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.14f));
            g2.setColor(Color.WHITE);
            g2.fill(new Ellipse2D.Double(w * 0.52, h * 0.52, w * 0.55, h * 0.58));
            g2.setComposite(saved);

            g2.setColor(new Color(255, 255, 255, 30));
            for (int x = 0; x < w; x += 44) {
                for (int y = 0; y < h; y += 44) {
                    g2.fillOval(x, y, 2, 2);
                }
            }

            g2.setStroke(new BasicStroke(1.2f));
            g2.setColor(new Color(255, 255, 255, 36));
            g2.drawRoundRect(18, 18, w - 37, h - 37, 32, 32);

            g2.setStroke(new BasicStroke(0.9f));
            Path2D shear = new Path2D.Double();
            shear.moveTo(0, h * 0.78);
            shear.quadTo(w * 0.35, h * 0.68, w * 0.7, h * 0.42);
            shear.quadTo(w * 0.88, h * 0.3, w, h * 0.08);
            g2.setColor(new Color(180, 230, 255, 42));
            g2.draw(shear);
            g2.dispose();
        }
    }
}
