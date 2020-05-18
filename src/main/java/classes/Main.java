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

        Bank bank = new Bank();
        ArrayList<Thread> threadList = new ArrayList<>();

        //добавлене счетов в банк
        for (int i = 0; i < 100; i++){
            bank.addAccount(new Account((long) (Math.random() * 10000000)));
        }

        //старт перводов между счетами
        for(int i = 0; i < 5000; i ++){
            Thread currentThread = new Thread(() -> {
                try {
                    long amount;
                    if (Math.random() > 0.05)
                        amount = (long) (Math.random() * 50000);
                    else
                        amount = (long) (Math.random() * 50000 + 60000);

                    bank.transfer(bank.getRandomAccount().getAccNumber(),
                            bank.getRandomAccount().getAccNumber(), amount);
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
        bank.getTransactionInfo(4000);
    }
}
