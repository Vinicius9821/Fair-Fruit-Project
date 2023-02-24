import { Container } from './styles';
import { memo, useContext, useEffect, useState } from 'react';
import axios from 'axios';
import { IconButton } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import { useFruitsContext } from 'common/contexts/Fruits';
import RemoveIcon from '@material-ui/icons/Remove';

function Fruits({ name, picture, id, price, unidade }) {
    const [products, setProduct] = useState([]);
    const { shopping, addFruit } = useFruitsContext();

    const getProducts = async () => {
        try {
            const response = await axios.get("/api/products");
            const data = response.data;
            setProduct(data);
            console.log(data);
        } catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        getProducts();
    }, [])

    return (
        <>
            {products.length === 0 ? <p>Loading...</p> : (
                products.map((product) => (
                    <Container className="get" key={product.id}>
                        <div>
                            <img
                                src={`${product.image}`}
                                alt={`${product.name}`}
                                width="80" height="70"
                            />
                            <p>
                                {product.name} - $ {product.price?.toFixed(2)} <span>Kg</span>
                            </p>
                        </div>
                        <div>
                            <IconButton>
                                <RemoveIcon/>
                            </IconButton>
                            <IconButton>
                                <AddIcon />
                            </IconButton>
                        </div>
                    </Container>
                ))
            )}
        </>
    );
}

export default memo(Fruits);
