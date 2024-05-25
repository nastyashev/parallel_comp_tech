import mpi.*;
import static java.lang.System.exit;

class Main {
    private static double[][] a;
    private static double[][] b;
    private static double[][] c;

    private static final int MASTER = 0;
    private static final int FROM_MASTER = 1;
    private static final int FROM_WORKER = 2;
    private static final int NRA = 2;
    private static final int NCA = 2;
    private static final int NCB = 2;
    private static final int NRB = NCA;
    private static final int NRC = NRA;
    private static final int NCC = NCB;
    private static final int N_SEND = 128;

    public static void main(String[] args) {
        a = new double[NRA][NCA];
        b = new double[NRB][NCB];
        c = new double[NRC][NCC];
        MPI.Init(args);

        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();

        if (size < 2) {
            System.out.println("Need at least two MPI tasks. Quitting...\\n");
            MPI.COMM_WORLD.Abort(13);
            exit(13);
        }

//        size = size - 1;
        if (rank == MASTER) {
            System.out.format("mpi_mm has started with %d tasks.%n", size);

            for (int i = 0; i < NRA; i++) {
                for (int j = 0; j < NCA; j++) {
                    a[i][j] = 10;
                }
            }
            for (int i = 0; i < NRB; i++) {
                for (int j = 0; j < NCB; j++) {
                    b[i][j] = 10;
                }
            }


//            int averow = NRA / size;
//            int extra = NRA % size;
//            int offset = 0;
            for (int dest = 1; dest < size; dest++) {

            }
        }
//            for (int dest = 1; dest <= size; dest++) {
//                int rows = (dest <= extra) ? averow + 1 : averow;
//                System.out.format("Sending %d rows to task %d offset=%d%n", rows, dest, offset);
//                int[] tmp = new int[1]; // Java MPI використовує масиви
//                tmp[0] = offset;
//                MPI.COMM_WORLD.Send(tmp, 0, 1, MPI.INT, dest, FROM_MASTER);
//                tmp[0] = rows; // Java MPI використовує масиви
//                MPI.COMM_WORLD.Send(tmp, 0, 1, MPI.INT, dest, FROM_MASTER);
//                for (int i = 0; i < NRA; i++) {
//                    MPI.COMM_WORLD.Send(a[i], 0, NCA, MPI.DOUBLE, dest, FROM_MASTER);
//                }
//                double[] bData = new double[NCA * NCB];
//                for (int i = 0; i < NCA; i++) {
//                    MPI.COMM_WORLD.Send(b[i], 0, NCB, MPI.DOUBLE, dest, FROM_MASTER);
//                }
//                offset = offset + rows;
//            }
//            int[] data = new int[1]; // Оголошення масиву для отримання даних
//
//            for (int source = 1; source <= size; source++) {
//                int[] tmp = new int[1];
//                MPI.COMM_WORLD.Recv(tmp, 0, 1, MPI.INT, source, FROM_WORKER);
//                offset = tmp[0];
//                MPI.COMM_WORLD.Recv(tmp, 0, 1, MPI.INT, source, FROM_WORKER);
//                int rows = tmp[0];
//                double[][] cBlock = new double[rows][NCB]; // Оголошення двовимірного масиву для отримання блоку даних
//                for (int i = 0; i < rows; i++) {
//                    MPI.COMM_WORLD.Recv(cBlock[i], 0, NCB, MPI.DOUBLE, source, FROM_WORKER);
//                }
//                System.out.format("Received results from task %d%n", source);
//            }
//            System.out.println("****");
//            System.out.println("Result Matrix:");
//            for (int i = 0; i < NRA; i++) {
//                System.out.println();
//                for (int j = 0; j < NCB; j++) {
//                    System.out.format("%6.2f ", c[i][j]);
//                }
//            }
//            System.out.println("\n****");
//            System.out.println("Done.");
//        } else {
//            int[] tmp = new int[1];
//            MPI.COMM_WORLD.Recv(tmp, 0, 1, MPI.INT, MASTER, FROM_MASTER);
//            int offset = tmp[0];
//            MPI.COMM_WORLD.Recv(tmp, 0, 1, MPI.INT, MASTER, FROM_MASTER);
//            int rows = tmp[0];
//
//            for (int i = 0; i < NRA; i++) {
//                MPI.COMM_WORLD.Recv(a[i], 0, NCA, MPI.DOUBLE, MASTER, FROM_MASTER);
//            }
//
//
//            double[] bData = new double[NCA * NCB];
//            MPI.COMM_WORLD.Recv(bData, 0, NCA * NCB, MPI.DOUBLE, MASTER, FROM_MASTER);
//            double[][] b = new double[NCA][NCB];
//            int index = 0;
//            for (int i = 0; i < NCA; i++) {
//                for (int j = 0; j < NCB; j++) {
//                    b[i][j] = bData[index++];
//                }
//            }
//
//            for (int i = 0; i < NCB; i++) {
//                for (int j = 0; j < rows; j++) {
//                    c[j][i] = 0.0;
//                    for (int k = 0; k < NCA; k++) {
//                        c[j][i] += a[j][k] * b[k][i];
//                    }
//                }
//            }
//
//            tmp[0] = offset; // Оголошення масиву для передачі одного значення
//            MPI.COMM_WORLD.Send(tmp, 0, 1, MPI.INT, MASTER, FROM_WORKER);
//            tmp[0] = rows; // Оголошення масиву для передачі одного значення
//            MPI.COMM_WORLD.Send(tmp, 0, 1, MPI.INT, MASTER, FROM_WORKER);
//            double[] cData = new double[rows * NCB];
//            index = 0;
//            for (int i = 0; i < rows; i++) {
//                for (int j = 0; j < NCB; j++) {
//                    cData[index++] = c[i][j];
//                }
//            }
//        }

        MPI.Finalize();
    }

    public static void sendMatrix(double[][] matrix, int dest) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[] tmp = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tmp[i * cols + j] = matrix[i][j];
            }
        }

        int nCountSend = Math.ceilDiv(rows * cols, N_SEND);
        for (int i = 0; i < nCountSend; i++) {
            int nSend = Math.min(N_SEND, rows * cols - i * N_SEND);
            MPI.COMM_WORLD.Send(tmp, i * N_SEND, nSend, MPI.DOUBLE, dest, FROM_MASTER);
        }
    }
}
