package classes;


import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        /**
         *Программа выполняет переводы между счетами банка в многопоточном режиме;
         *
         * Для проверки гернерируется 100 аккаунтов и выполняется 5000 запросов на
         * выполнение транзакции между случайными счетами;
         *
         * С вероятностью 5% сумма перевода между счетами задается больше 50000.
         *  При этом после выполнения транзакций счета могут быт заблокированы
         *  после проверки выполненной транзакции службой безопасности;
         *
         *  В консоль после выполнения программы выводится общая информация по деятельности
         *  банка, а также информация по определенным транзакциям;
         */

        final int ACCOUNT_NUM = 10;
        final int TRANSFER_NUM = 1000000;
        final int THREAD_NUM = 10;

        Bank bank = new Bank();
        ArrayList<Thread> threadList = new ArrayList<>();

        //добавлене счетов в банк
        for (int i = 0; i < ACCOUNT_NUM; i++){
            bank.addAccount(new Account((long) (Math.random() * 10000000)));
        }

        //вывод суммы на счетах клиентов до начала переводов
        bank.showMoneyOnAccounts();

        //старт перводов между счетами
        for(int i = 0; i < THREAD_NUM; i ++){
            Thread currentThread = new Thread(() -> {
                try {
                    for (int k = 0; k < TRANSFER_NUM; k++) {
                        long amount;
                        if (Math.random() > 0.0001)
                            amount = (long) (Math.random() * 50000);
                        else
                            amount = (long) (Math.random() * 50000 + 60000);

                        //выполнение перевода между счетами
                        bank.transfer(bank.getRandomAccount(ACCOUNT_NUM).getAccNumber(),
                                bank.getRandomAccount(ACCOUNT_NUM).getAccNumber(), amount);
                    }
            }
                catch (InterruptedException e){
                    System.out.println(e.getMessage());
                }
            });
            currentThread.start();
            threadList.add(currentThread);
        }

        //ожидание завершения потоков
        for (Thread thread : threadList)
            thread.join();

        bank.getBankInfo();
        System.out.println();
        bank.getTransactionInfo(1);
        //вывод суммы на счетах клиентов до начала переводов
        bank.showMoneyOnAccounts();
    }
}
