package example4;

import example4.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by zhuyichen on 2017/8/9.
 */
@Mapper
public interface StudentMapper {
    @Select("SELECT * FROM student where studentId = #{id}")
    Student findByStudentId(@Param("id") String StudentId);

    @Insert("INSERT INTO student (studentId, name, age) VALUES (#{studentId}, #{name}, #{age})")
    void addStudent(@Param("studentId") String studentId, @Param("name") String name, @Param("age") int age);
}