package edu.java.shared.config;

public record TopicInfo(
    String name,
    int partitions,
    int replicas
) {}
