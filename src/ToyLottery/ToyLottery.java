package ToyLottery;

import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;

public class ToyLottery {
    private class Toy {
        private int id;
        private String name;
        private int quantity;
        private double weight;

        public Toy(int id, String name, int quantity, double weight) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.weight = weight;
        }
    }

    private PriorityQueue<Toy> toyQueue;

    public ToyLottery() {
        toyQueue = new PriorityQueue<>((t1, t2) -> {
            // Сортировка по убыванию веса (частоты выпадения игрушки)
            if (t1.weight > t2.weight) {
                return -1;
            } else if (t1.weight < t2.weight) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    public void addToy(int id, String name, int quantity, double weight) {
        Toy toy = new Toy(id, name, quantity, weight);
        toyQueue.add(toy);
    }

    public void changeWeight(int id, double newWeight) {
        Toy targetToy = null;
        for (Toy toy : toyQueue) {
            if (toy.id == id) {
                targetToy = toy;
                break;
            }
        }
        if (targetToy != null) {
            toyQueue.remove(targetToy);
            targetToy.weight = newWeight;
            toyQueue.add(targetToy);
        } else {
            System.out.println("Игрушка с указанным id не найдена.");
        }
    }

    public void startLottery() {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("queue.txt");
        } catch (IOException e) {
            System.out.println("Не удалось открыть файл для записи.");
            return;
        }

        for (int i = 0; i < 35; i++) {
            Toy winningToy = getToy();
            if (winningToy != null) {
                try {
                    fileWriter.write((i + 1) + ". Призовая игрушка: " + winningToy.name + "\n");
                    fileWriter.flush();
                } catch (IOException e) {
                    System.out.println("Не удалось записать данные в файл.");
                }
            }
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Не удалось закрыть файл.");
        }
    }

    private Toy getToy() {
        if (!toyQueue.isEmpty()) {
            Toy toy = toyQueue.poll();
            toy.quantity--;
            if (toy.quantity > 0) {
                toyQueue.add(toy);
            }
            return toy;
        }
        return null;
    }

    public static void main(String[] args) {
        ToyLottery toyLottery = new ToyLottery();
        // Добавление игрушек и их веса
        toyLottery.addToy(1, "Мяч", 5, 20);
        toyLottery.addToy(2, "Кукла", 3, 15);
        toyLottery.addToy(3, "Машинка", 7, 30);
        toyLottery.addToy(4, "Конструктор", 7, 10);
        toyLottery.addToy(5, "Мишка", 10, 13);
        // Изменение веса
        toyLottery.changeWeight(2, 10); // Изменение веса игрушки с id 2 на 10
        // Запуск розыгрыша
        toyLottery.startLottery();
    }
}