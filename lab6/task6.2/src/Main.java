import mpi.*;
import static java.lang.System.exit;

class Main {
    private static double[][] a; // Матриця А
    private static double[][] b; // Матриця В
    private static double[][] c; // Матриця С

    private static final int MASTER = 0; // taskid мастера
    private static final int FROM_MASTER = 1; // повідомлення від мастера
    private static final int FROM_WORKER = 2; // повідомлення від робітника
    private static final int NRA = 62; // рядки матриці А
    private static final int NCA = 15; // стовпці матриці А
    private static final int NRB = NCA; // рядки матриці В
    private static final int NCB = 7; // стовпці матриці В
    private static final int NRC = NRA; // рядки матриці С
    private static final int NCC = NCB; // стовпці матриці С
    private static final int N_SEND = 128; // кількість елементів, які можна відправити за один раз
    private static int N_CALC; // кількість елементів, які потрібно обчислити кожному робітнику

    public static void main(String[] args) {
        // Ініціалізація матриць
        a = new double[NRA][NCA];
        b = new double[NRB][NCB];
        c = new double[NRC][NCC];

        // Ініціалізація MPI
        MPI.Init(args);
        int size = MPI.COMM_WORLD.Size(); // кількість потоків
        int rank = MPI.COMM_WORLD.Rank(); // номер потоку

        Main.N_CALC = Math.ceilDiv(NRC * NCC, size - 1);

        // Якщо потоків 2 і менше, то завершуємо роботу
        if (size < 2) {
            System.out.println("Need at least two MPI tasks. Quitting...");
            MPI.COMM_WORLD.Abort(13);
            exit(13);
        }

        // Якщо потік мастер
        if (rank == MASTER) {
            System.out.format("mpi_mm has started with %d tasks.%n", size - 1);

            // Ініціалізація матриць
            for (int i = 0; i < NRA; i++) {
                for (int j = 0; j < NCA; j++) {
                    a[i][j] = Math.random() * 10;
                }
            }
            for (int i = 0; i < NRB; i++) {
                for (int j = 0; j < NCB; j++) {
                    b[i][j] = Math.random() * 10;
                }
            }

            // Розподіл матриць між робітниками
            for (int dest = 1; dest < size; dest++) {
                int len = Math.min(N_CALC, NRC * NCC - (dest - 1) * N_CALC); // кількість елементів, які потрібно обчислити робітнику
                sendInt(len, dest, FROM_MASTER); // відправляємо кількість елементів
                int offset = (dest - 1) * N_CALC; // зміщення для робітника
                sendInt(offset, dest, FROM_MASTER); // відправляємо зміщення
                sendMatrix(a, dest, FROM_MASTER); // відправляємо матрицю А
                sendMatrix(b, dest, FROM_MASTER); // відправляємо матрицю В
            }

            // Отримання результатів від робітників
            for (int source = 1; source < size; source++) {
                int offset = receiveInt(source, FROM_WORKER); // отримуємо зміщення
                int len = receiveInt(source, FROM_WORKER); // отримуємо кількість елементів
                double[][] cBlock = receiveMatrix(source, NRC, NCC, FROM_WORKER); // отримуємо блок матриці С

                // Записуємо блок матриці С в матрицю С
                for (int i = 0; i < len; i++) {
                    int row = Math.floorDiv((offset + i), NCC); // рядок в матриці С
                    int col = (offset + i) % NCC; // стовпець в матриці С
                    c[row][col] = cBlock[row][col]; // записуємо елемент в матрицю С
                }
            }

            // Виведення результату
            System.out.println("Result Matrix:");
            for (int i = 0; i < NRC; i++) {
                System.out.println();
                for (int j = 0; j < NCC; j++) {
                    System.out.format("%6.2f ", c[i][j]);
                }
            }
        }
        // Якщо потік робітник
        else {
            int len = receiveInt(MASTER, FROM_MASTER); // отримуємо кількість елементів
            int offset = receiveInt(MASTER, FROM_MASTER); // отримуємо зміщення
            double[][] a = receiveMatrix(MASTER, NRA, NCA, FROM_MASTER); // отримуємо матрицю А
            double[][] b = receiveMatrix(MASTER, NRB, NCB, FROM_MASTER); // отримуємо матрицю В

            // Обчислення блоку матриці С
            for (int i = 0; i < len; i++) {
                int row = (offset + i) / NCC; // рядок в матриці С
                int col = (offset + i) % NCC; // стовпець в матриці С
                c[row][col] = 0; // обнулення елемента матриці С
                for (int j = 0; j < NCA; j++) {
                    c[row][col] += a[row][j] * b[j][col]; // обчислення елемента матриці С
                }
            }

            sendInt(offset, MASTER, FROM_WORKER); // відправляємо зміщення
            sendInt(len, MASTER, FROM_WORKER); // відправляємо кількість елементів
            sendMatrix(c, MASTER, FROM_WORKER); // відправляємо блок матриці С

        }

        MPI.Finalize();
    }

    /**
     * Надсилання матриці на потік dest з тегом from
     * @param matrix матриця
     * @param dest номер потоку
     * @param from тег
     */
    private static void sendMatrix(double[][] matrix, int dest, int from) {
        int rows = matrix.length; // кількість рядків
        int cols = matrix[0].length; // кількість стовпців
        double[] tmp = new double[rows * cols]; // тимчасовий масив для зберігання матриці
        // Перетворення матриці в масив
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tmp[i * cols + j] = matrix[i][j];
            }
        }

        int nCountSend = Math.ceilDiv(rows * cols, N_SEND); // кількість блоків, які потрібно відправити
        // Відправлення блоків
        for (int i = 0; i < nCountSend; i++) {
            int nSend = Math.min(N_SEND, rows * cols - i * N_SEND); // кількість елементів в поточному блоку
            MPI.COMM_WORLD.Send(tmp, i * N_SEND, nSend, MPI.DOUBLE, dest, from); // відправлення блоку
        }
    }

    /**
     * Отримання матриці від потоку source з тегом from
     * @param source номер потоку
     * @param rows кількість рядків
     * @param cols кількість стовпців
     * @param from тег
     * @return матриця
     */
    private static double[][] receiveMatrix(int source, int rows, int cols, int from) {
        double[][] matrix = new double[rows][cols]; // матриця
        double[] tmp = new double[rows * cols]; // тимчасовий масив для зберігання матриці
        int nCountRecv = Math.ceilDiv(rows * cols, N_SEND); // кількість блоків, які потрібно отримати
        // Отримання блоків
        for (int i = 0; i < nCountRecv; i++) {
            int nRecv = Math.min(N_SEND, rows * cols - i * N_SEND); // кількість елементів в поточному блоку
            MPI.COMM_WORLD.Recv(tmp, i * N_SEND, nRecv, MPI.DOUBLE, source, from); // отримання блоку
        }

        // Перетворення масиву в матрицю
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = tmp[i * cols + j];
            }
        }

        return matrix;
    }

    /**
     * Відправлення цілого числа на потік dest з тегом from
     * @param value ціле число
     * @param dest номер потоку
     * @param from тег
     */
    private static void sendInt(int value, int dest, int from) {
        int[] tmp = new int[1]; // тимчасовий масив для зберігання цілого числа
        tmp[0] = value; // записуємо ціле число в масив
        MPI.COMM_WORLD.Send(tmp, 0, 1, MPI.INT, dest, from); // відправлення цілого числа
    }

    /**
     * Отримання цілого числа від потоку source з тегом from
     * @param source номер потоку
     * @param from тег
     * @return ціле число
     */
    private static int receiveInt(int source, int from) {
        int[] tmp = new int[1]; // тимчасовий масив для зберігання цілого числа
        MPI.COMM_WORLD.Recv(tmp, 0, 1, MPI.INT, source, from); // отримання цілого числа
        return tmp[0]; // повертаємо ціле число
    }
}
