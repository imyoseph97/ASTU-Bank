package banking;

import banking.service.BankService;
import banking.store.BankDataStore;
import banking.ui.BankingAppUI;

public class Main {
    public static void main(String[] args) {
        BankDataStore dataStore = new BankDataStore("data/bank_data.ser");
        BankService bankService = new BankService(dataStore);
        new BankingAppUI(bankService).show();
    }
}
