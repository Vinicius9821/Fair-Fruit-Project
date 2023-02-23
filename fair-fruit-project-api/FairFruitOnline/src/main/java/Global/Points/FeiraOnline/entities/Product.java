package Global.Points.FeiraOnline.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_name")
    @NotEmpty(message = "{field.description.name}")
    private String name;

    @Column(name = "unit_price")
    @NotNull(message = "{field.price.required}")
    private BigDecimal price;

    @Column(name = "image")
    private String image;

}
