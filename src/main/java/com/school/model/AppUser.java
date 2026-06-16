package com.school.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // Имя разделено на фамилию и имя (японский порядок отображения: фамилия, затем имя).
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Уникальный 6-значный номер. Заполняется только для студентов (для остальных — null).
    @Column(unique = true)
    private String studentNumber;

    // Имя файла фотографии на диске (uploads/), либо null, если фото не загружено.
    private String photoName;

    // Заполняется только для студентов: к какой группе относится.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    // Отображаемое полное имя: «Фамилия Имя» (для шаблонов и сортировки в UI).
    @Transient
    public String getFullName() {
        String last = lastName == null ? "" : lastName;
        String first = firstName == null ? "" : firstName;
        return (last + " " + first).trim();
    }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getPhotoName() { return photoName; }
    public void setPhotoName(String photoName) { this.photoName = photoName; }

    public SchoolClass getSchoolClass() { return schoolClass; }
    public void setSchoolClass(SchoolClass schoolClass) { this.schoolClass = schoolClass; }
}
