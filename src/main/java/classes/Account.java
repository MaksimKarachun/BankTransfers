package classes;

import TransactionExceptions.*;

public class Account
{
    private long money;
    private String accNumber;
    private boolean accountWork;

    public Account(long money) {
        this.money = money;
        accNumber = generationAccNumber();
        accountWork = true;
    }

    private String generationAccNumber(){
        int accLength = 16;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < accLength; i++){
            int num = (int)(Math.random() * 10);
            sb.append(num);
        }
        return sb.toString();
    }

    public long getMoney() {
        return money;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public boolean getStateOfAccount() {
        return accountWork;
    }

    public void setStateOfAccount(Boolean accountWork) {
        this.accountWork = accountWork;
    }

    public void putMoneyToAcc(long amount) throws AccountLockedException {
        //если аккаунт заблокирован генерируем исключение
        if (!accountWork)
            throw new AccountLockedException("Acount " + this.accNumber + " locked. Cant put money on account.");
        else
            money = money + amount;
    }

    public void getMoneyFromAcc(long amount) throws BallanceException, AccountLockedException {
        //если аккаунт заблокирован генерируем исключение
        if (!accountWork)
            throw new AccountLockedException("Acount " + this.accNumber + " locked. Cant get money from account.");
        else {
            //если не хватает средств для выполнения ранзакции генерируем исключение
            if (money - amount >= 0) {
                money = money - amount;
            }
            else
                throw new BallanceException("Not enough money in the account");
        }
    }

    public void returnMoney(long amount){
        money = money + amount;
    }
}
