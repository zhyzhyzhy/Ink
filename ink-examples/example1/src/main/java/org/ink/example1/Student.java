package org.ink.example1;

public class Student {
    private String studentId;
    private int age;
    private String addr;

    public Student() {
    }

    public Student(String studentId, int age, String addr) {
        this.studentId = studentId;
        this.age = age;
        this.addr = addr;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Student{");
        sb.append("studentId='").append(studentId).append('\'');
        sb.append(", age=").append(age);
        sb.append(", addr='").append(addr).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
