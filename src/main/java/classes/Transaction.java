package classes;

import TransactionExceptions.AccountLockedException;
import TransactionExceptions.BallanceException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Transaction {
    private ArrayList<String> statusList = new ArrayList<>();
    private static int count = 0;
    private int id = 0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private boolean done = false;

    public Transaction() {
        count++;
        id = count;
        statusList.add(dateFormat.format(new Date()) + " ||||Create new transaction");
    }

    public synchronized void doTransaction(Account fromAcc, Account toAcc, long amount){

        //добавляем статус начала транзакции
        statusList.add(dateFormat.format(new Date()) + " ||||Start transfer transaction from account: " + fromAcc.getAccNumber() + ", to account: " + toAcc.getAccNumber());

        try {
            statusList.add(dateFormat.format(new Date()) + " ||||Get money from account " + fromAcc.getAccNumber() + " - start");
            fromAcc.getMoneyFromAcc(amount);
            statusList.add(dateFormat.format(new Date()) + " ||||Get money from account " + fromAcc.getAccNumber() + " - successful");

            statusList.add(dateFormat.format(new Date()) + " ||||Put money to account " + fromAcc.getAccNumber() + " - start");
            toAcc.putMoneyToAcc(amount);
            statusList.add(dateFormat.format(new Date()) + " ||||Put money to account " + fromAcc.getAccNumber() + " - successful");

            done = true;
            statusList.add(dateFormat.format(new Date()) + " ||||Transaction complete");

        } catch (BallanceException ballanceException) {
            statusList.add(ballanceException.getMessage());
            statusList.add(dateFormat.format(new Date()) + " ||||Transaction was stopped.");
        }
        catch (AccountLockedException accountException){
            statusList.add(accountException.getMessage());
            statusList.add(dateFormat.format(new Date()) + " ||||Transaction stopped.");
        }
    }

    //получение статусов выполнения транзакции
    public void getTransactionInfo(){
        System.out.println("================================================================================================================");
        System.out.printf("%s%04d%s%n","=============================================Transaction ",id," info==============================================");
        System.out.println("================================================================================================================");
        for(String current : statusList){
            System.out.println(current);
        }
        System.out.println("================================================================================================================");
        System.out.println();
    }

    public boolean transactionSuccessful(){
        return done;
    }
}
