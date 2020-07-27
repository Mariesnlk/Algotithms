
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.util.Scanner;

public class PercolationStats {

    private int N;
    private int T;
    private double[] result;

    // perform independent trials on an n-by-n grid
    private PercolationStats(int N, int trials) throws OutOfMemoryError {
        this.N = N;
        this.T = trials;
        result = new double[this.T];
        for (int i = 0; i < this.T; i++) {
            result[i] = doExperiment();
        }

    }

    private double doExperiment() {
        Percolation percolation = new Percolation(N);
        int amountOfSites = N * N;
        do {
            int stepOfOpenedSites;
            do {
                stepOfOpenedSites = StdRandom.uniform(amountOfSites);
            } while (percolation.isOpen(getRow(stepOfOpenedSites), getColumn(stepOfOpenedSites)));
            percolation.open(getRow(stepOfOpenedSites), getColumn(stepOfOpenedSites));
        } while (!percolation.percolates());

        return (double) percolation.numberOfOpenSites() / (N * N);

    }

    // sample mean of percolation threshold
    private double mean() {
        return StdStats.mean(result);
    }

    // sample standard deviation of percolation threshold
    private double stddev() {
        return StdStats.stddev(result);
    }

    private int getRow(int num) {
        return (num / N) + 1;
    }

    private int getColumn(int point) {
        return (point % N) + 1;
    }

    // low endpoint of 95% confidence interval
    private double confidenceLo() {
        return StdStats.mean(result) - ((1.96 * StdStats.stddev(result)) / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    private double confidenceHi() {
        return StdStats.mean(result) + ((1.96 * StdStats.stddev(result)) / Math.sqrt(T));
    }

    // запускаємо експерименти і рахуємо відповідні значення середнє,
    // відхилення, інтервал довіри, та виводимо на екран
    public static void main(String[] args) {
        int N;
        int T;
        int restart = 1;

        Scanner in = new Scanner(System.in);
        while (restart == 1) {
            try {
                System.out.println("Enter N ");
                N = in.nextInt();
                if (N <= 1) {
                    System.out.println("You are wrong! Try one more time ");
                    continue;
                }
                System.out.println("Enter T");
                T = in.nextInt();
                if (T <= 1) {
                    System.out.println("You are wrong! Try one more time ");
                    continue;
                }
                PercolationStats stats = new PercolationStats(N, T);
                System.out.println("mean = " + stats.mean());
                System.out.println("stddev = " + stats.stddev());
                System.out.println("95% confidence interval = " + stats.confidenceLo());
                System.out.println(stats.confidenceHi());


            } catch (OutOfMemoryError e) {
                System.out.println("Out of memory. Please enter a smaller array dimension.");
                continue;

            } catch (Exception e) {
                System.out.println("Wrong symbol");
                continue;
            }
            try {
                System.out.println("--Enter 1 to restart the program");
                restart = in.nextInt();
                if (restart != 1) System.out.println("You finish the program");
            } catch (Exception e) {
                System.out.println("You finish the program");
            }
        }
    }
}