package example4;

import com.noname.db.Service;
import com.noname.ioc.annotation.Inject;
import example4.Student;
import example4.StudentMapper;

/**
 * Created by zhuyichen on 2017/8/9.
 */
@Service
public class StudentService {
    @Inject
    private StudentMapper studentMapper;
    public Student findByStudentId(String studentId) {
        return studentMapper.findByStudentId(studentId);
    }

    public void addStudent(String studentId, String name, int age) {
        studentMapper.addStudent(studentId, name, age);
    }
}
