package com.project.teachmteachybackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Post için userId boş olamaz")
    private Long userId;

    @NotNull
    @NotBlank(message = "Post başlığı boş olamaz")
    private String title;

    // -- 1 --
    @Lob
    @Column(columnDefinition="text")
    private String content;

    private Date created_at;
}

/*
-- 1 --
Bu örnekte, @Lob anotasyonu text adlı bir alanın üzerine yerleştirilmiştir.
@Column(columnDefinition="text") ifadesi de bu alanın text türünde bir sütun
olarak veritabanında oluşturulmasını sağlar. Bu, bu alanda uzun metin verileri
saklamak için kullanıldığı anlamına gelir.

 -- 2 --
Postlar userId ile user a bağlı o yüzden burada bir user nesnesi oluşturmamız lazım.
Daha sonra @ManyToOne ile birçok postun bir user'a ait olabileceğini belirtiyoruz.
FetchType.Lazy ile user objesine sahip olan postu çekersen user'ı çekmemesini istiyoruz.
Eğer FetchType.EAGER yapsaydık post objesinin içerisinde user da gelecekti.
JoinColumn ile bu postun içerisindeki user'ın user tablosundaki user'a bağlandığını belirtiyoruz.
nullable'a false vererek buranın boş olmamasını belirtiyoruz.
OnDelete e cascade değerini vererek bir user silindiğinde onun tüm postlarının silinmesini istiyoruz.
JsonIgnore ile bu alanı es geç diyoruz zaten bunu json yapısında çekmeyeceğiz.
 */