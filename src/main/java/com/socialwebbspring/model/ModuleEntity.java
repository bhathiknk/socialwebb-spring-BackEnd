package com.socialwebbspring.model;

import javax.persistence.*;

@Entity
@Table(name = "module")
public class ModuleEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "module_name", columnDefinition = "TEXT")
        private String moduleName;

        @Column(name = "user_id")
        private Integer userId;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }
    }

