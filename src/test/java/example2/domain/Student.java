package example2.domain;

public class Student {
    private String studentId;
    private String password;

    public Student(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Student{");
        sb.append("studentId='").append(studentId).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
