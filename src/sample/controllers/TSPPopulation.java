package sample.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TSPPopulation {

    private List<TSPChromosome> population; // quần thể
    private final int initialSize; // kich thuoc cua quan the

    TSPPopulation(final TSPGene[] points,
                  final int initialSize) {
        this.population = init(points, initialSize);
        this.initialSize = initialSize;
    }

    List<TSPChromosome> getPopulation() {
        return this.population;
    }

    TSPChromosome getAlpha() {
        return this.population.get(0);
    }

    // trộn các gen trong 1 Choromosome
    private List<TSPChromosome> init(final TSPGene[] points, final int initialSize) {
        final List<TSPChromosome> eden = new ArrayList<>();
        for(int i = 0; i < initialSize; i++) {
            final TSPChromosome chromosome = TSPChromosome.create(points);
            eden.add(chromosome); // add vào eden
        }
        return eden;
    }

    void update() {
        doCrossOver();
        doMutation();
        doSpawn();
        doSelection();
    }

    // chọn lọc
    private void doSelection() {
        this.population.sort(Comparator.comparingDouble(TSPChromosome::getDistance)); // sắp xếp theo thứ tự tăng dần distance
        this.population = this.population.stream().limit(this.initialSize).collect(Collectors.toList());
    }

    // them 1000 cá thể mới vào quần thể
    private void doSpawn() {
        IntStream.range(0, 1000).forEach(e -> this.population.add(TSPChromosome.create(TSPUtils.CITIES)));
    }

    // đột biến
    private void doMutation() {
        final List<TSPChromosome> newPopulation = new ArrayList<>();
        for(int i = 0; i < this.population.size()/10; i++) {
            final TSPChromosome mutation = this.population.get(TSPUtils.randomIndex(this.population.size())).mutate();
            newPopulation.add(mutation);
        }
        this.population.addAll(newPopulation);
    }

    // lai tạo
    private void doCrossOver() {
        final List<TSPChromosome> newPopulation = new ArrayList<>();
        for(final TSPChromosome chromosome : this.population) {
            final TSPChromosome partner = getCrossOverPartner(chromosome);
            newPopulation.addAll(Arrays.asList(chromosome.crossOver(partner)));
        }
        this.population.addAll(newPopulation);
    }

    private TSPChromosome getCrossOverPartner(final TSPChromosome chromosome) {
        TSPChromosome partner = this.population.get(TSPUtils.randomIndex(this.population.size())); // lấy 1 cá thể trong quần thể
        while(chromosome == partner) {
            partner = this.population.get(TSPUtils.randomIndex(this.population.size())); // lấy 1 cá thể mới khác chormosome
        }
        return partner;
    }
}
