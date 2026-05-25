/*
1.	Yoseph Shewatatek				UGR/38267/17
2.	Bethelhem Gezahegn			    UGR/36556/17
3.	Tayu Yismu						UGR/37834/17
4.	Natan Habtamu					UGR/38258/17
*/





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
