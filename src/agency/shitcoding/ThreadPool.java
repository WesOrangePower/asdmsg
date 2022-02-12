package agency.shitcoding;

import java.util.HashMap;

public class ThreadPool {
    // Предел потоков работающих одновременно
    private final int threadCountLimit;

    // Счётчик всех зарегистрированных потоков
    // с начала запуска
    private Integer counter = 0;

    // Список потоков
    private final HashMap<Integer,Thread> threadList;

    public ThreadPool(int threadCount) {
        this.threadCountLimit = threadCount;
        this.threadList = new HashMap<>(threadCount);
    }

    // Обрабатывает клиента: создаёт для него отдельный поток,
    // создаёт все необходимые связи (база данных, канал между потоками)
    public boolean handleClient(Object client){
        // Если лимит на количество потоков превышен,
        // то клиент игнорируется
        if (threadCountLimit == threadList.size()){
            return false;
        }

        // Уникальный номер потока
        final Integer threadID = this.counter;

        Runnable runnable = () -> {
            /*

                Какие-то действия клиента

            */


            // Конец потока: удаление из списка потоков
            synchronized (ThreadPool.this.threadList) {
                ThreadPool.this.threadList.remove(threadID);
            }
        };
        Thread client_thread = new Thread(runnable);

        // Добавление потока в списко потоков
        this.threadList.put(threadID, client_thread);
        client_thread.start();

        this.counter++;

        return true;
    }
}
