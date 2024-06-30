package com.retail.rewards.entity;

import com.retail.rewards.util.PrimaryKeyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@Builder
@ToString(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @GenericGenerator(name = "custom-primary-key-generator", type = PrimaryKeyGenerator.class)
    @GeneratedValue(generator = "custom-primary-key-generator")
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private List<TransactionEntity> transactionEntities;

    @Column(name = "deleted")
    private Boolean deleted;

}