package banking;
import java.util.ArrayList;
import java.util.*;
public class Bank {
	private ArrayList<Account> accounts;
	private Scanner scanner;
	private Database database;
	
	public Bank(){
		accounts = new ArrayList<>();
		scanner  = new Scanner(System.in);
		database = new Database();
		loadAccountsFromdatabase();
	}
	public void start() {
		while(true) {
			System.out.println("\n --- Welcome to the banking application ---");
			System.out.println("1. Create Account");
			System.out.println("2. Deposit");
			System.out.println("3. Withdraw");
			System.out.println("4. Check Balance");
			System.out.println("5. View Transaction History");
			System.out.println("6. Transfer funds");
			System.out.println("7. Generate reports");
			System.out.println("8. Update");
			System.out.println("9. Exit");
			System.out.println("Choose an option: ");
			int choice = scanner.nextInt();
			switch(choice) {
			case 1:
				createAccount();
				break;
			case 2:
				deposit();
				break;
			case 3:
				withdraw();
				break;
			case 4:
				viewAccount();
			case 5:
				viewTransactionHistory();
				break;
			case 6:
				viewTransferFunds();
				break;
			case 7:
				generateReportsmenu();
				break;
			case 8:
				System.out.println("Enter account ID to update: ");
				int accountId = scanner.nextInt();
				update(accountId);
				break;
			case 9:
				System.out.println("Thank you for using the Banking application");
				database.closeConnection();
				return;
			default:
				System.out.println("Invalid option try again");
			}
		}
	}
	private void createAccount() {
	    System.out.print("Enter customer ID: ");
	    long customerId = scanner.nextInt();
		 long length = String.valueOf(customerId).length();
		    if (length < 8 || length > 16) {
		        System.out.println("The Customer ID must be between 8 and 16 digits!");
		        return;
		    }


	    System.out.print("Enter account type (s for Savings, c for Current): ");
	    char accountTypeInput = scanner.next().charAt(0);
	    String accountType;

	    if (accountTypeInput == 's' || accountTypeInput == 'S') {
	        accountType = "Savings";
	    } else if (accountTypeInput == 'c' || accountTypeInput == 'C') {
	        accountType = "Current";
	    } else {
	        System.out.println("Invalid account type entered. Defaulting to Savings.");
	        accountType = "Savings";
	    }
	    System.out.print("Enter your name: ");
	    scanner.nextLine();
	    String name = scanner.nextLine();
	    scanner.nextLine();
	    System.out.print("Enter your phone number: ");
	    String phoneNumber = scanner.nextLine();
	    if (phoneNumber.length() != 10) {
	        System.out.println("Phone Number must be 10 digits.");
	        return;
	    }

	    System.out.print("Enter your address: ");
	    String address = scanner.nextLine();
	    Account account = new Account(customerId,accountType,name,phoneNumber,address);
	    database.insertAccount(account);
	    System.out.println("Account created successfully! your account ID: "+account.getAccountid());
	    }
	
	private void deposit() {
	    System.out.print("Enter account ID: ");
	    int accountId = scanner.nextInt();

	    Account account = findAccount(accountId);

	    if (account != null) {
	        System.out.print("Enter amount to deposit: ");
	        double amount = scanner.nextDouble();

	        account.deposit(amount);
	        database.updateAccountBalance(account);

	        System.out.println("Deposit successful! New balance: " + account.getBalance());
	    } else {
	        System.out.println("Account not found.");
	    }
	}
	private void withdraw() {
	    System.out.print("Enter account ID: ");
	    int accountId = scanner.nextInt();

	    Account account = findAccount(accountId);

	    if (account != null) {
	        System.out.print("Enter amount to withdraw: ");
	        double amount = scanner.nextDouble();

	        if (account.withdraw(amount)) {
	            database.updateAccountBalance(account);
	            System.out.println("Withdrawal successful! New balance: " + account.getBalance());
	        } else {
	            System.out.println("Insufficient funds.");
	        }
	    } else {
	        System.out.println("Account not found.");
	    }
	}
	private void viewAccount() {
		System.out.println("Enter Account ID:");
		int acccountId = scanner.nextInt();
		Account account = findAccount(acccountId);
		
		if(account != null) {
			System.out.print("Account name "+ account.getName());
			System.out.print("Account Id "+ account.getAccountid());
			System.out.print("Account Address "+ account.getAddress());
			System.out.print("Account Phone_no "+ account.getPhoneNumber());
			System.out.print("Customer Id  "+ account.getCustomerId());
			System.out.print("Customer Id  "+ account.getBalance());
		}
		else {
			System.out.print("Account not found");
		}
	}
	private void viewTransactionHistory() {
		System.out.print("Enter account ID: ");
		int accountId = scanner.nextInt();
		Account account = findAccount (accountId);
		if (account != null) {
		account.printtransactionHistory();
		} else {
		System.out.println("Account not found.");
		}
	}
	private void viewTransferFunds() {
	    System.out.print("Enter source account ID: ");
	    int accountId = scanner.nextInt();
	    Account sourceAccount = findAccount(accountId); 

	    if (sourceAccount == null) {
	        System.out.println("Source account not found.");
	        return;
	    }

	    System.out.print("Enter destination account ID: ");
	    int destinationAccountId = scanner.nextInt();
	    Account destinationAccount = findAccount(destinationAccountId); 
	    if (destinationAccount == null) {
	        System.out.println("Destination account not found.");
	        return;
	    }

	    System.out.print("Enter amount to transfer: ");
	    double amount = scanner.nextDouble();
	    
	    if(amount<=0) {
	    	System.out.println("transfer amount must be positive");
	    	return;
	    }
	    if (sourceAccount.getBalance() < amount) {
	    	System.out.println("insufficien funds in the source account, available balance : " + sourceAccount.getBalance());
	    }
	    sourceAccount.withdraw(amount);
	    destinationAccount.deposit(amount);
	    database.updateAccountBalance(sourceAccount);
	    database.updateAccountBalance(destinationAccount);
	    System.out.printf("Successfully transferring %.2f from account ID : %d to account ID : %d.%n",amount,accountId,destinationAccountId);
	}
	private Account findAccount(int accountId) {
	    for (Account account : accounts) {
	        if (account.getAccountid() == accountId) {
	            return account;
	        }
	    }
	    return null;
	}
	private void loadAccountsFromdatabase() {
		accounts = database.getAllAccounts();
	}
	private void generateReportsmenu() {
		while(true) {
			System.out.println("1. Customer Report");
			System.out.println("2. Total Account type");
			System.out.println("3. Total Amount");
			System.out.println("4. Exit");
			
			System.out.println("Enter your choice: ");
			int choice = scanner.nextInt();
			
			switch(choice) {
			case 1 :
				customerReport();	
				break;
				
			case 2 :
				totalAccountType();
				break;
				
			case 3 :
				totalAmount();
				break;
			
			case 4 :
				return;
			
			default :
				System.out.println("Enter a valid choice!");
			}
		}
	}
	
	private void customerReport() {

		 System.out.println("Customer Details Report");
		 System.out.println("-------------------------------------------------------------");
		 System.out.printf(String.format("%-15s %-20s %-15s %-10s %-10s %-15s %-10s%n", 
				 "Account Id", "Customer Id","Account Type","Balance","Name","Phone Number","Address"));
	     System.out.println("-------------------------------------------------------------");
	        for (Account account : accounts) {
	            System.out.printf(String.format("%-15s %-20s %-15s %-10s %-10s %-15s %-10s%n",
	            		account.getAccountid(),
	            		account.getCustomerId(),
	            		account.getaccountType(),
	            		account.getBalance(),
	            		account.getName(),
	            		account.getPhoneNumber(),
	            		account.getAddress()));
	        }
	}
	
	private void totalAccountType() {
		int totalSavingsAccount=0,totalCurrentAccount=0;
		for(Account account : accounts) {
			if(account.getaccountType().equalsIgnoreCase("Savings")) {
				totalSavingsAccount += 1;
			}
			if(account.getaccountType().equalsIgnoreCase("Current")) {
				totalCurrentAccount += 1;
			}
		}
		System.out.println("Total Savings Accounts: "+ totalSavingsAccount);
		System.out.println("Total Current Accounts: "+ totalCurrentAccount);
	}
	
	private void totalAmount() {
		double sum = 0;
		for(Account account : accounts) {
			sum += account.getBalance();
			}
		System.out.println("Total Amount in bank is: "+sum);
	}
	
	private void update(int accountId) {
		Account account = findAccount(accountId);
		if(account != null) {
			System.out.println("Customer details");
			System.out.println("Name: " + account.getName());
			System.out.println("Phone number: " + account.getPhoneNumber());
			System.out.println("Address: " + account.getAddress());
		}
		else {
			System.out.println("Account not found");
		}
			
			System.out.println("Enter New Name(or press enter to keep current): ");
			scanner.nextLine();
			String name = scanner.nextLine();
			if(!name.isEmpty()) {
				account.setName(name);
			}
			
			System.out.println("Enter New Phone number: ");
			scanner.nextLine();
			String phoneNumber = scanner.nextLine();
			if(!phoneNumber.isEmpty()) {
				account.setphoneNumber(phoneNumber);
			}
			
			System.out.println("Enter New Address: ");
			scanner.nextLine();
			String address = scanner.nextLine();
			if(!address.isEmpty()) {
				account.setAddress(address);
			}
			database.updateAccountDetails(account);
			System.out.println("Details Updated Successfully!!!");
		}	
}