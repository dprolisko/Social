package com.dprol.social.entity.user;

import com.dprol.social.entity.Country;
import com.dprol.social.entity.Jira;
import com.dprol.social.entity.Premium;
import com.dprol.social.entity.Rating;
import com.dprol.social.entity.contact.Contact;
import com.dprol.social.entity.contact.ContactPreference;
import com.dprol.social.entity.event.Event;
import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.entity.skill.Skill;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name= "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 32, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 32, nullable = false)
    private String password;

    @Column(name = "email", length = 64, nullable = false, unique = true)
    private String email;

    @Column(name = "phone", length = 16, nullable = false, unique = true)
    private String phone;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "bio", length = 512)
    private String bio;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "users")
    private List<Skill> listSkill;

    @OneToOne(mappedBy ="user")
    private Jira jira;

    @OneToOne(mappedBy = "user")
    private Premium premium;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fileId", column = @Column(name = "profile_pic_file_id")),
            @AttributeOverride(name = "smallFileId", column = @Column(name = "profile_pic_small_file_id"))
    })
    private UserProfile userProfile;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "city_id", length = 64)
    private String city;

    @OneToMany(mappedBy = "inviter_id")
    private List<GoalInvitation> inviter;

    @OneToMany(mappedBy = "invited_id")
    private List<GoalInvitation> invited;

    @ManyToMany(mappedBy = "users")
    private List<Goal> goals;

    @OneToMany(mappedBy = "user_id")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "user_id")
    private List<Contact> contacts;

    @OneToOne(mappedBy = "user_id")
    private ContactPreference contactPreference;

    @OneToMany(mappedBy = "user")
    private List<Event> event;

    @ManyToMany
    @JoinTable(name = "user_event", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> participatedEvents;

    @ManyToMany
    @JoinTable(name = "user_follower", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<User> followers;

    @ManyToMany
    @JoinTable(name = "user_following", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    private List<User> followings;
}
