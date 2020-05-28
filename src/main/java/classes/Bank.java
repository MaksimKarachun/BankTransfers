package classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Bank
{
    private int accountIndex;
    private Map<Integer, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public synchronized boolean isFraud()
        throws InterruptedException
    {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {

            int fromAccIndex = getAccountIndex(fromAccountNum);
            int toAccIndex = getAccountIndex(toAccountNum);

            Account fromAccount = accounts.get(fromAccIndex);
            Account toAccount = accounts.get(toAccIndex);
            //если счет заблокирован выводим сообщение о невозможности выполнить транзкцию
            if (!fromAccount.getStateOfAccount() ||
                    !toAccount.getStateOfAccount()) {
                return;
            }

            if (fromAccIndex < toAccIndex) {
                synchronized (fromAccount) {
                    synchronized (toAccount) {
                        //выполнение транзакции
                        doTransaction(fromAccount, toAccount, amount);
                    }
                }
            }
            else
                synchronized (toAccount) {
                    synchronized (fromAccount) {
                        //выполнение транзакции
                        doTransaction(fromAccount, toAccount, amount);
                    }
                }

                //если сумма перевода больше 50000 то после проведения, транзакция проверяется службой безопасности
                if (amount > 50000) {
                    if (isFraud()) {
                        fromAccount.setStateOfAccount(false);
                        toAccount.setStateOfAccount(false);
                    }
                }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accNum)
    {
        return accounts.get(getAccountIndex(accNum)).getMoney();
    }

    public void addAccount(Account account){
        accountIndex++;
        accounts.put(accountIndex, account);
    }

    //метод выполняет транзакцию
    private void doTransaction(Account fromAcc, Account toAcc, long amount){
                Transaction transaction = new Transaction();
                //transactionsList.add(transaction);
                transaction.doTransaction(fromAcc, toAcc, amount);

            /*проверка на выполнение(невыполнение) транзакции по переводу средств.
            инкрементирование соответствующего счетчика.*/
                /*if (transaction.transactionSuccessful())
                    successfulTransactions.incrementAndGet();
                else
                    stoppedTransactions.incrementAndGet();*/
    }

    //метод возвращает индекс аккаунта по его номеру
    public Integer getAccountIndex(String accountNum){
        int index = 0;
        for(Map.Entry<Integer, Account> entry : accounts.entrySet()){
            Account acc = entry.getValue();
            if(acc.getAccNumber().equals(accountNum))
                index = entry.getKey();
        }
        return index;
    }

    /*получение случайного аккаунта из списка аккаунтов банка.
    метод для проверки работы программы.*/
    public Account getRandomAccount(Integer accNum){
        return accounts.get((int) ((Math.random() * accNum) + 1));
    }

    //получение итоговой информации по работе банка
    /*public void getBankInfo(){
        System.out.println("================================================================");
        System.out.println("======================Bank Information list=====================");
        System.out.println("================================================================");
        System.out.println("Общее количество запросов на перевод средств: " + count);
        System.out.println("Количество отказов в выполнении операции: " + blockedTransaction);
        System.out.println("================================================================");
        System.out.println("Общее количество транзакций: " + transactionsList.size());
        System.out.println("Количecтво выполненных транзакций: " + successfulTransactions);
        System.out.println("Количecтво остановленных транзакций: " + stoppedTransactions);
        System.out.println("================================================================");
    }*/

    /*public void getTransactionInfo(int id){
        transactionsList.get(id).getTransactionInfo();
    }*/

    public void showMoneyOnAccounts(){
        long sum = 0;
        for (Map.Entry<Integer, Account> entry : accounts.entrySet()){
            sum = sum + entry.getValue().getMoney();
        }
        System.out.println("На счетах клиентов: " + sum);
    }
}
