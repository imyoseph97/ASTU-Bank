# ASTU Bank System (Java OOP + Swing)

A polished desktop banking application with:
- OOP backend (models, services, storage, security).
- Modern Swing frontend with gradients, polished cards, dashboard tabs, and transaction table.
- Secure password hashing (`SHA-256`) and persisted account data.
- Deposit, withdrawal, transfer, password change (with old password), and transaction history.
- Student ID validation in `UGR/XXXXX/XX` format (auto uppercase).

## Project layout

| Folder | Contents |
|--------|----------|
| `src/main/java/banking/` | All packages (same as usual Java coursework layout) |
| `data/` | Runtime data (`bank_data.ser` + optional `users/*.txt`) |
| `docs/` | `PROJECT_LAYOUT.txt`, step-by-step code guide `ASTU_Bank_Code_Walkthrough.pdf` (regenerate with `python docs/generate_code_walkthrough_pdf.py`) |
| `scripts/` | Windows helper `run.bat` to compile and launch |

## Default demo users
- `UGR/00001/24` / `1234`
- `UGR/00002/24` / `1111`
- `UGR/00003/24` / `2222`

## Run
From project root:

```bash
javac --release 17 -encoding UTF-8 -d out src/main/java/banking/Main.java src/main/java/banking/model/*.java src/main/java/banking/security/*.java src/main/java/banking/service/*.java src/main/java/banking/store/*.java src/main/java/banking/ui/*.java
java -cp out banking.Main
```

On Windows you can double-click or run:

`scripts/run.bat`

Data is saved to `data/bank_data.ser`.
Per-user files are also created in `data/users/`.
