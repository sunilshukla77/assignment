package com.cc.assignment.entity;

import com.cc.assignment.model.Event;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "EventDetail")
@Getter
@Setter
@ToString
public class EventDetail {
        @Id
        @JsonProperty("id")
        private String id;

        @JsonProperty("duration")
        private int duration;

        @JsonProperty("type")
        private String type;

        @JsonProperty("host")
        private String host;

        @JsonProperty("alert")
        private Boolean alert;

        public EventDetail(){
        }
        public EventDetail(Event event, int duration){
                this.id = event.getId();
                this.type = event.getType();
                this.host = event.getHost();
                this.duration = duration;
                this.alert = Boolean.FALSE;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
                EventDetail that = (EventDetail) o;
                return id != null && Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
                return getClass().hashCode();
        }
}
