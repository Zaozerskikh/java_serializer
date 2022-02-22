package ru.hse.homework4;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Mapper {
    /**
     * Считывание объекта из строки.
     * @param clazz тип объекта, который надо считать.
     * @param input строка, из которой надо считать объект.
     * @param <T> типизирующий параметр класса объекта.
     * @return десериализованный объект.
     * @throws UnsupportedOperationException если десериализовать объект (ошибка валидации класса объекта).
     * @throws IllegalArgumentException если входная строка некорректного формата.
     */
    <T> T readFromString(Class<T> clazz, String input) throws UnsupportedOperationException, IllegalArgumentException;


    /**
     * Считывание объекта из потока.
     * @param clazz тип объекта, который надо считать.
     * @param inputStream поток, из которого надо считать объект.
     * @param <T> типизирующий параметр класса объекта.
     * @return десериализованный объект.
     * @throws IOException если проблема с входным потоком.
     * @throws UnsupportedOperationException если десериализовать объект (ошибка валидации класса объекта).
     * @throws IllegalArgumentException если строка во входном потоке некорректного формата.
     */
    <T> T read(Class<T> clazz, InputStream inputStream) throws IOException, UnsupportedOperationException, IllegalArgumentException;


    /**
     * Считывание объекта из файла.
     * @param clazz тип объекта, который надо считать.
     * @param file файл, из которого надо считать объект.
     * @param <T> типизирующий параметр класса объекта.
     * @return десериализованный объект.
     * @throws IOException если проблема с входным файлом.
     * @throws UnsupportedOperationException если десериализовать объект (ошибка валидации класса объекта).
     * @throws IllegalArgumentException если строка во входном файле некорректного формата.
     */
    <T> T read(Class<T> clazz, File file) throws IOException, UnsupportedOperationException, IllegalArgumentException;


    /**
     * Запись объекта в строку.
     * @param object объект.
     * @return строковое представление объекта согласно формату.
     * @throws UnsupportedOperationException если сериализовать объект невозможно.
     */
    String writeToString(Object object) throws UnsupportedOperationException;


    /**
     * Запись объекта в поток данных согласно формату.
     * @param object объект.
     * @param outputStream поток.
     * @throws IOException если ошибка связана с потоком.
     * @throws UnsupportedOperationException если сериализовать объект невозможно.
     */
    void write(Object object, OutputStream outputStream) throws IOException, UnsupportedOperationException;


    /**
     * Запись объекта в файл согласно формату.
     * @param object объект.
     * @param file файл.
     * @throws IOException если ошибка связана с файлом.
     * @throws UnsupportedOperationException если сериализовать объект невозможно.
     */
    void write(Object object, File file) throws IOException, UnsupportedOperationException;
}
