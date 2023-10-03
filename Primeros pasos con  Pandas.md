En este artículo aprenderemos en qué consiste una de las librerias más famosas del lenguaje Python. Trataremos de explicar desde su instalación hasta realizar una pequeña prueba para ir entendiendo como trabajar con esta librería. Este artículo va a ser el inicio de otros con el que iremos profundizando los conocimientos y viendo posibles aplicaciones de Machine Learning, Big Data o Data Science.


## Tabla de cotenidos
+ [1. Introducción](#1)
+ [2. Instalación](#2)
+ [3. DataFrames](#3)
+ [4. Series](#4)
+ [5. Leer fichero CSV](#5)
+ [6. Manipulación de datos](#6)
+ [7. Conclusiones](#7)
+ [Enlaces y referencias](#8)


### 1. Introducción <a name="1"></a>
Pandas es un paquete de Python que proporciona estructuras de datos similares a los dataframes de R. Es importante señalar aquí que, dado que pandas lleva a cabo tareas importantes, como alinear datos para su comparación, fusionar conjuntos de datos, gestión de datos perdidos, se ha convertido en una librería muy importante para procesar datos a alto nivel en Python (es decir, estadísticas). Fue diseñada originalmente para gestionar datos financieros, y como alternativo al uso de hojas de cálculo.

Esta librería depende de Numpy, que añade un potente tipo matricial a Python. Los principales tipos de datos que pueden representarse con pandas son:
 - Datos heterogéneos (o datos tabulares, como son llamados comúnmente)
 - Series temporales.
 
Pandas proporciona herramientas que permiten:
- Leer y escribir datos en diferentes formatos: CSV, Microsoft Excel, bases SQL y formato HDF5
- Seleccionar y filtrar de manera sencilla tablas de datos en función de posición, valor o etiquetas
- Fusionar y unir datos
- Transformar datos aplicando funciones tanto en global como por ventanas
- Manipulación de series temporales
- Hacer gráficas por medio de matplotlib o seaborn, entre otros.

En pandas existen tres tipos básicos de objetos todos ellos basados a su vez en Numpy:
- Series (listas, 1D),
- DataFrame (tablas, 2D) y
- Panels (tablas 3D).

Nosotros vamos a ver el uso básico de los dos primeros tipos de objetos, para un mayor detalle puedes consultar la [documentación oficial](https://pandas.pydata.org/pandas-docs/stable/user_guide/dsintro.html).




### 2. Instalación <a name="2"></a>
La forma más fácil de instalar Pandas es instalarlos como parte de la distribución [Anaconda](https://docs.continuum.io/anaconda/), una distribución multiplataforma para el análisis de datos y la computación científica. Este es el método de instalación recomendado para la mayoría de los usuarios.

En este caso, hemos preferido usar el propio instalador de Python. Antes de nada, como requisito imprescindible, es tener Python3 y Numpy instalados en nuestro equipo. Dicho esto, instalar Pandas es tan sencillo como ejecutar el siguiente comando:



```bash
pip3 install pandas
```




### 3. DataFrames <a name="3"></a>
Para trabajar con datos tabulares (filas y columnas), pandas incluye la versátil estructura DataFrame. Un DataFrame o frame se puede entender como una colección de Series (columnas), todas compartiendo un listado de índices únicos. La forma más común de crear un frame es con un diccionario en el que cada clave se asocia a un listado de elementos de igual longitud.


```python
import pandas as pd

diccionario = { "nombre" : ["Marisa","Laura","Manuel"], "edad" : [34,29,11] }
frame = pd.DataFrame(diccionario)

display(frame)
```


<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>nombre</th>
      <th>edad</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>0</th>
      <td>Marisa</td>
      <td>34</td>
    </tr>
    <tr>
      <th>1</th>
      <td>Laura</td>
      <td>29</td>
    </tr>
    <tr>
      <th>2</th>
      <td>Manuel</td>
      <td>11</td>
    </tr>
  </tbody>
</table>
</div>


Como podemos observar, la clave se utiliza como nombre de cada columna (serie), y cada elemento se
asocia a una fila en función del índice.



### 4. Series <a name="4"></a>
Las Series son estructuras unidimensionales similares a las ndarray de NumPy, en las que cada elemento posee también un índice único. Se pueden crear tanto a partir de listas como de diccionarios. La forma más sencilla de crear una serie es con el constructor pd.Series(<lista>). 
    
Pandas asume índices de 0 a n-1 (donde n es el tamaño de la <lista> de elementos). De manera opcional podemos especificar una lista con las etiquetas de las filas.



```python
serie = pd.Series(['Barcelona','Madrid','Valencia','Sevilla'])
print(serie, '\n')
serie2 = pd.Series(['Barcelona','Madrid','Valencia','Sevilla'], index=['a','b','c','d'])
print(serie2)
```

    0    Barcelona
    1       Madrid
    2     Valencia
    3      Sevilla
    dtype: object 
    
    a    Barcelona
    b       Madrid
    c     Valencia
    d      Sevilla
    dtype: object





### 5. Leer fichero CSV <a name="5"></a>
La lectura de fichero es realmente sencilla con Pandas. Con su método read_csv() le podemos pasar la ruta online para que se lo descargue o si ya tenemos uno en nuestro equipo, solo tendríamos que poner la ruta de este para que pueda leerlo. 

Aunque nosotros hemos usado read_csv, tiene muchos más para poder cargar datos que se encuentren en ficheros con distintas extensiones. En la [documentación](https://pandas.pydata.org/pandas-docs/stable/reference/io.html) podemos ver el resto de implementaciones.



```python
bills = pd.read_csv("https://raw.githubusercontent.com/mwaskom/seaborn-data/master/tips.csv")
display(bills.sample(10))
```


<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>total_bill</th>
      <th>tip</th>
      <th>sex</th>
      <th>smoker</th>
      <th>day</th>
      <th>time</th>
      <th>size</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>115</th>
      <td>17.31</td>
      <td>3.50</td>
      <td>Female</td>
      <td>No</td>
      <td>Sun</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
    <tr>
      <th>26</th>
      <td>13.37</td>
      <td>2.00</td>
      <td>Male</td>
      <td>No</td>
      <td>Sat</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
    <tr>
      <th>24</th>
      <td>19.82</td>
      <td>3.18</td>
      <td>Male</td>
      <td>No</td>
      <td>Sat</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
    <tr>
      <th>122</th>
      <td>14.26</td>
      <td>2.50</td>
      <td>Male</td>
      <td>No</td>
      <td>Thur</td>
      <td>Lunch</td>
      <td>2</td>
    </tr>
    <tr>
      <th>155</th>
      <td>29.85</td>
      <td>5.14</td>
      <td>Female</td>
      <td>No</td>
      <td>Sun</td>
      <td>Dinner</td>
      <td>5</td>
    </tr>
    <tr>
      <th>32</th>
      <td>15.06</td>
      <td>3.00</td>
      <td>Female</td>
      <td>No</td>
      <td>Sat</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
    <tr>
      <th>231</th>
      <td>15.69</td>
      <td>3.00</td>
      <td>Male</td>
      <td>Yes</td>
      <td>Sat</td>
      <td>Dinner</td>
      <td>3</td>
    </tr>
    <tr>
      <th>79</th>
      <td>17.29</td>
      <td>2.71</td>
      <td>Male</td>
      <td>No</td>
      <td>Thur</td>
      <td>Lunch</td>
      <td>2</td>
    </tr>
    <tr>
      <th>243</th>
      <td>18.78</td>
      <td>3.00</td>
      <td>Female</td>
      <td>No</td>
      <td>Thur</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
    <tr>
      <th>12</th>
      <td>15.42</td>
      <td>1.57</td>
      <td>Male</td>
      <td>No</td>
      <td>Sun</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
  </tbody>
</table>
</div>


Con bills.sample(10) conseguimos obtener del DataFrame 10 valores aleatorios del conjunto de datos para poder echar un vistado como está estructurada nuestra tabla. 



### 6. Manipulación de datos <a name="6"></a>
Dado que en el paso anterior ya tenemos un conjunto de datos vamos a proceder a realizar unas operaciones para ver el potencial de esta librería, ya que con pocas líneas de código obtendremos el resultado esperado.


```python
# Saber la media de tips
mean = bills['tip'].mean()
print(f'Media = {mean:.2f}')

# Hacer una máscara para obtener aquellas bills cuyo total es superior a 15
bills = bills[bills['total_bill'] > 15]
display(bills.sample(10))

# Ver cuantos son fumadores y cuantos no y cuyo total es superior a 15 
print(bills['smoker'].value_counts())

# Si solo queremos imprimir las columnas total_bill y day haremos lo siguiente
display(bills.loc[:, ['total_bill', 'day']].sample(5))
```

    Media = 3.46



<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>total_bill</th>
      <th>tip</th>
      <th>sex</th>
      <th>smoker</th>
      <th>day</th>
      <th>time</th>
      <th>size</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>15</th>
      <td>21.58</td>
      <td>3.92</td>
      <td>Male</td>
      <td>No</td>
      <td>Sun</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
    <tr>
      <th>160</th>
      <td>21.50</td>
      <td>3.50</td>
      <td>Male</td>
      <td>No</td>
      <td>Sun</td>
      <td>Dinner</td>
      <td>4</td>
    </tr>
    <tr>
      <th>3</th>
      <td>23.68</td>
      <td>3.31</td>
      <td>Male</td>
      <td>No</td>
      <td>Sun</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
    <tr>
      <th>101</th>
      <td>15.38</td>
      <td>3.00</td>
      <td>Female</td>
      <td>Yes</td>
      <td>Fri</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
    <tr>
      <th>134</th>
      <td>18.26</td>
      <td>3.25</td>
      <td>Female</td>
      <td>No</td>
      <td>Thur</td>
      <td>Lunch</td>
      <td>2</td>
    </tr>
    <tr>
      <th>227</th>
      <td>20.45</td>
      <td>3.00</td>
      <td>Male</td>
      <td>No</td>
      <td>Sat</td>
      <td>Dinner</td>
      <td>4</td>
    </tr>
    <tr>
      <th>17</th>
      <td>16.29</td>
      <td>3.71</td>
      <td>Male</td>
      <td>No</td>
      <td>Sun</td>
      <td>Dinner</td>
      <td>3</td>
    </tr>
    <tr>
      <th>20</th>
      <td>17.92</td>
      <td>4.08</td>
      <td>Male</td>
      <td>No</td>
      <td>Sat</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
    <tr>
      <th>231</th>
      <td>15.69</td>
      <td>3.00</td>
      <td>Male</td>
      <td>Yes</td>
      <td>Sat</td>
      <td>Dinner</td>
      <td>3</td>
    </tr>
    <tr>
      <th>93</th>
      <td>16.32</td>
      <td>4.30</td>
      <td>Female</td>
      <td>Yes</td>
      <td>Fri</td>
      <td>Dinner</td>
      <td>2</td>
    </tr>
  </tbody>
</table>
</div>


    No     100
    Yes     64
    Name: smoker, dtype: int64



<div>
<style scoped>
    .dataframe tbody tr th:only-of-type {
        vertical-align: middle;
    }

    .dataframe tbody tr th {
        vertical-align: top;
    }

    .dataframe thead th {
        text-align: right;
    }
</style>
<table border="1" class="dataframe">
  <thead>
    <tr style="text-align: right;">
      <th></th>
      <th>total_bill</th>
      <th>day</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>211</th>
      <td>25.89</td>
      <td>Sat</td>
    </tr>
    <tr>
      <th>65</th>
      <td>20.08</td>
      <td>Sat</td>
    </tr>
    <tr>
      <th>243</th>
      <td>18.78</td>
      <td>Thur</td>
    </tr>
    <tr>
      <th>34</th>
      <td>17.78</td>
      <td>Sat</td>
    </tr>
    <tr>
      <th>25</th>
      <td>17.81</td>
      <td>Sat</td>
    </tr>
  </tbody>
</table>
</div>


La cantidad de operaciones que podemos hacer es realmente grande y por eso no podemos poner todas pero os comparto la documentación en la sección de [enlaces y referencias](#8) para que podáis investigar por vosotros mismos y buscar aquellas funcionalidades que resuelvan vuestro problema en concreto.  



### 7. Conclusiones <a name="7"></a>
Esto ha sido un artículo introductorio para entender un poco qué es la librería y qué cosas se puede hacer con ella. También hemos visto lo que es una DataFrame y una Serie, y hemos jugueteado un poco con ellos. En el próximo artículo haremos un caso más real y mostraremos otra alternativa a la que dió Juan Antonio, en el siguiente [artículo](https://www.adictosaltrabajo.com/2019/10/30/leer-un-json-con-r-rstudio/) con R.



### Enlaces y referencias <a name="8"></a>
- [Guía de usuario de Pandas](https://pandas.pydata.org/pandas-docs/stable/user_guide/index.html)
- Como Pandas depende de Numpy, también podemos usar sus operaciones. [Numpy](https://numpy.org/doc/stable/user/)
