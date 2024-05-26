import mpi.MPI;

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
    private static int N_CALC;

    public static void main(String[] args) {
        a = new double[NRA][NCA];
        b = new double[NRB][NCB];
        c = new double[NRC][NCC];
        MPI.Init(args);

        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();

        Main.N_CALC = Math.ceilDiv(NRC * NCC, size - 1);

        if (size < 2) {
            System.out.println("Need at least two MPI tasks. Quitting...");
            MPI.COMM_WORLD.Abort(13);
            exit(13);
        }

        if (rank == MASTER) {
            System.out.format("mpi_mm has started with %d tasks.%n", size - 1);

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

            for (int dest = 1; dest < size; dest++) {
                int len = Math.min(N_CALC, NRC * NCC - (dest - 1) * N_CALC);
                sendInt(len, dest, FROM_MASTER);
                int offset = (dest - 1) * N_CALC;
                sendInt(offset, dest, FROM_MASTER);
                sendMatrix(a, dest, FROM_MASTER);
                sendMatrix(b, dest, FROM_MASTER);
            }

            for (int source = 1; source < size; source++) {
                int offset = receiveInt(source, FROM_WORKER);
                int len = receiveInt(source, FROM_WORKER);
                double[][] cBlock = receiveMatrix(source, NRC, NCC, FROM_WORKER);

                for (int i = 0; i < len; i++) {
                    int row = Math.floorDiv(offset, NCC);
                    int col = offset % NCC;
                    c[row][col] = cBlock[i][0];
                }
            }

            System.out.println("****");
            System.out.println("Result Matrix:");
            for (int i = 0; i < NRA; i++) {
                System.out.println();
                for (int j = 0; j < NCB; j++) {
                    System.out.format("%6.2f ", c[i][j]);
                }
            }
            System.out.println("\n****");
            System.out.println("Done.");
        } else {
            int len = receiveInt(MASTER, FROM_MASTER);
            int offset = receiveInt(MASTER, FROM_MASTER);
            double[][] a = receiveMatrix(MASTER, NRA, NCA, FROM_MASTER);
            double[][] b = receiveMatrix(MASTER, NRB, NCB, FROM_MASTER);

            for (int i = 0; i < len; i++) {
                int row = (offset + i) / NCC;
                int col = (offset + i) % NCC;
                c[row][col] = 0;
                for (int j = 0; j < NCA; j++) {
                    c[row][col] += a[row][j] * b[j][col];
                }
            }

            sendInt(offset, MASTER, FROM_WORKER);
            sendInt(len, MASTER, FROM_WORKER);
            sendMatrix(c, MASTER, FROM_WORKER);

        }

        MPI.Finalize();
    }

    private static void sendMatrix(double[][] matrix, int dest, int from) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[] tmp = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                tmp[i * rows + j] = matrix[i][j];
            }
        }

        int nCountSend = Math.ceilDiv(rows * cols, N_SEND);
        for (int i = 0; i < nCountSend; i++) {
            int nSend = Math.min(N_SEND, rows * cols - i * N_SEND);
//            System.out.println("Send: " + tmp + " " + i * N_SEND + " " + nSend + " " + MPI.DOUBLE + " " + dest + " " + from);
            MPI.COMM_WORLD.Send(tmp, i * N_SEND, nSend, MPI.DOUBLE, dest, from);
        }
    }

    private static double[][] receiveMatrix(int source, int rows, int cols, int from) {
        double[][] matrix = new double[rows][cols];
        double[] tmp = new double[rows * cols];
        int nCountRecv = Math.ceilDiv(rows * cols, N_SEND);
        for (int i = 0; i < nCountRecv; i++) {
            int nRecv = Math.min(N_SEND, rows * cols - i * N_SEND);
//            System.out.println("Recv: " + tmp + " " + i * N_SEND + " " + nRecv + " " + MPI.DOUBLE + " " + source + " " + from + " " + (rows + " " + cols + " " + i + " " + N_SEND));
            MPI.COMM_WORLD.Recv(tmp, i * N_SEND, nRecv, MPI.DOUBLE, source, from);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = tmp[i * cols + j];
            }
        }

        return matrix;
    }

    private static void sendInt(int value, int dest, int from) {
        int[] tmp = new int[1];
        tmp[0] = value;
        MPI.COMM_WORLD.Send(tmp, 0, 1, MPI.INT, dest, from);
    }

    private static int receiveInt(int source, int from) {
        int[] tmp = new int[1];
        MPI.COMM_WORLD.Recv(tmp, 0, 1, MPI.INT, source, from);
        return tmp[0];
    }
}
