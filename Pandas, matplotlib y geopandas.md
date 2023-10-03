## Pandas, Matplotlib y Geopandas

+ [1. Introducción](#1)
+ [2. Entender los datasheets](#2)
+ [3. Toma de decisiones](#3)
+ [4. Conclusiones](#4)
+ [Enlaces y referencias](#5)

<br>

### 1. Introducción<a name="1"></a>

Cuando decides comprar o alquilar una vivienda es importante tener conocimiento sobre que rango de valores puede rondar el inmueble. Esto es importante ya que te permite reducir el riesgo de inversión o incluso maximizar la rentabilizad a la hora de invertir. 

El objetivo de este artículo es resolver el problema que hizo Juan Antonio en el <a href="https://www.adictosaltrabajo.com/2019/10/30/leer-un-json-con-r-rstudio/" target="_blank">siguiente artículo</a> pero esta vez cambiando la tecnología, en nuestro caso lo haremos con Python. De esta forma, ampliamos el abaníco de posibilidades a la hora de resolver un problema. 

<br>


### 2. Entender los datasheets<a name="2"></a>

Para realizar el análisis vamos a hacer uso de dos datasets públicos que nos ofrece Idealista. El primero de ellos, será el <a href="https://idealista.carto.com/tables/paradas_metro_madrid/public" target="_blank">dataset con la información de las paradas de metro de Madrid</a> y el segundo, será el <a href="https://idealista.carto.com/api/v2/sql?q=select%20*%20from%20public.precio_metro_201605" target="_blank">dataset que nos dice el precio por m2 de compra y alquiler a 500m de dichas paradas de metro</a>

Comenzamos con el análisis del primer conjunto de datos para conocer que es cada campo. En primer lugar, importamos las librerias necesarias para leer un json a través del protocolo http.


```python
import pandas as pd
import json
import requests
```


```python
def read_from_url(url):
    uri = str(url).replace(' ', '%20')
    response = requests.get(uri)
    content = response.content.decode('utf-8')
    return json.loads(content)
```


```python
data = read_from_url('https://idealista.carto.com:443/api/v2/sql?q=select * from public.paradas_metro_madrid')
paradas_metro = pd.DataFrame(data['rows'])
display(paradas_metro.sample(5))
```

Echando un ojo al dataframe, vemos que de las paradas de metro nos interesan sólo algunos campos.

+ **cartodb_id**: es un índice númerico propio de la BBDD
+ **the_geom y the_geom_webmercator** son campos propios de un estándar GIS llamado <a href="https://es.wikipedia.org/wiki/GeoJSON" target="_blank">GeoJSON</a>
+ **place**: es el lugar que ocupa la estación en la línea
+ **id**: identificador único de todas las estaciones
+ **name**: nombre de la parada de metro
+ **line**: línea de metro
+ **lat y lng**: son coordenadas terrestres (latitud y longitud)

De estos campos, nos vamos a quedar sólo con id, name, line, place, lat y lng

Vamos a realizar la misma operación, pero ahora para el otro dataset.


```python
data = read_from_url('https://idealista.carto.com:443/api/v2/sql?q=select * from public.precio_metro_201605')
precio_metro = pd.DataFrame(data['rows'])
display(precio_metro.sample(5))
```

Analizando el dataframe, podemos observar las siguientes columnas.

+ **cartodb_id**: es un índice númerico propio de la BBDD
+ **the_geom y the_geom_webmercator** son campos propios de un estándar GIS llamado <a href="https://es.wikipedia.org/wiki/GeoJSON" target="_blank">GeoJSON</a> como en el caso anterior
+ **date**: fecha de registro
+ **id**: identificador único de la estaciones
+ **name**: nombre de la parada de metro
+ **sale**: precio del metro cuadrado para una venta
+ **rental**: precio del metro cuadrado de un alquiler

De este dataframe, solo nos vamos a quedar con id, sale, rental.

Sabiendo los datos que queremos de cada dataframe, vamos a proceder a hacer un merge da ambos de las columnas que nos interesan. 


```python
# Limpiamos dataframe primero
paradas_metro = paradas_metro.loc[:, ['id', 'name', 'line', 'place', 'lat', 'lng']]
precio_metro = precio_metro.loc[:,['id', 'sale', 'rental']]

# realizamos un merge de ambos frames
paradas_metro_precio = paradas_metro.merge(precio_metro, how="inner")
display(paradas_metro_precio.sample(5))
```

<br>

### 3. Toma de decisiones<a name="3"></a>

Hasta este punto, tenemos un dataframe relleno de datos pero sin saber muy bien que hacer con ellos. Vamos a proponer una serie de preguntas y responderlas para los distintos casos que puedan aparecer.
- 1. Soy un inversor y quiero obtener una gráfica del rendimiento de los inmuebles.
- 2. Tengo dinero ahorrado y quiero saber que beneficio obtengo por cada € invertido
- 3. Me gustaría cambiar de casa y quiero saber los precios y cuántos inmuebles hay en cada linea de metro

Antes tenemos que importar la librería de matplotlib para poder graficar los datos


```python
import matplotlib.pyplot as plt
```


```python
# 1
def performance():
    paradas_metro_precio['performance'] = (paradas_metro_precio['rental'] * 12) / paradas_metro_precio['sale']
    return paradas_metro_precio.sort_values(by='performance', ascending=False)
    
    
df_performance = performance()
h = df_performance.plot(x='line', y='performance', kind='scatter')
plt.show()
```


![png](https://i.ibb.co/xFpPTgZ/output-9-0.png)


Como podemos observar, la L1 es la que genera mayor rendimiento pero depende de la parada que eligas, ya que tiene algunas que cuyo rendimiento es del 4%. La L11 puede ser una mejor alternativa para invertir ya que toda la línea tiene un rendimiento entre el 7% y el 9% aproximadamente.

Para hayar el rendimiento que hay en cada parada de metro, hemos planteado la siguiente ecuación: $performance = \frac{12mounths * rental}{sale} · 100$


```python
# 2
def benefit(amount):
    df_aux = df_performance
    df_aux[f'benefit{int(amount/1000)}k'] = round(df_aux['performance'] * amount, 2)
    return df_aux.sort_values(by=f'benefit{int(amount/1000)}k', ascending=False)
    
amount = 10000
df_benefit = benefit(amount)
h = df_benefit[df_benefit['line'] == 'L11'].plot(x='name', y=f'benefit{int(amount/1000)}k')
plt.xticks(rotation=15)
plt.show()
```

    
![png](https://i.ibb.co/K2pwH6b/output-11-0.png)
    


Como en la pregunta anteior hemos visto que a la L11 puede ser una de las mejores alternativas para invertir, hemos calculado el beneficio que hay en cada parada de esas líneas. Por tanto, como vemos en la gráfica, la mejor parada donde buscar un inmueble sería Abrantes, ya que genera más del 9%, y la peor sería La Fortuna, porque solo obtienes algo menos del 7% por cada € invertido. 

Para saber el beneficio que hemos obtenido hemos realizado la siguiente ecuación: $benefit = performance · amount$

Para la pregunta 3, vamos hacer uso de Geopandas que nos permite posicionar correctamente los datos de nuestro conjunto a través de una nueva columna, que se generará a continuación, cuyos valores son en formato GeoJSON. 


```python
# 3
import geopandas as gpd
import os
```


```python
path = os.path.join('res', '20cc5389-1e0a-4d3b-8804-742ae1c71587202034-1-1j5kf1k.9aj3.shp')

# Cargar datos
locs_metro = gpd.read_file(path, encoding='utf-8')

print(locs_metro)
# Limpiar datos
locs_metro = locs_metro[locs_metro['CORONATARI'] == 'A'].loc[:, ['DENOMINACI', 'geometry']]
locs_metro['name'] = locs_metro['DENOMINACI'].str.lower()
df_benefit['name'] = df_benefit['name'].str.lower()
# del locs_metro['DENOMINACI']

#Merge y verificar que el dataframe resultante es correcto
mrg = locs_metro.merge(df_benefit, on='name')
display(mrg.sample(4))
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
      <th>geometry</th>
      <th>name</th>
      <th>id</th>
      <th>line</th>
      <th>place</th>
      <th>lat</th>
      <th>lng</th>
      <th>sale</th>
      <th>rental</th>
      <th>performance</th>
      <th>benefit10k</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <th>27</th>
      <td>POINT (442994.523 4476738.133)</td>
      <td>cartagena</td>
      <td>172</td>
      <td>L7</td>
      <td>17</td>
      <td>40.439336</td>
      <td>-3.672158</td>
      <td>3462.437488</td>
      <td>14.086485</td>
      <td>0.048820</td>
      <td>488.20</td>
    </tr>
    <tr>
      <th>22</th>
      <td>POINT (440135.509 4474628.991)</td>
      <td>callao</td>
      <td>112</td>
      <td>L5</td>
      <td>17</td>
      <td>40.420082</td>
      <td>-3.705667</td>
      <td>3749.744169</td>
      <td>15.280450</td>
      <td>0.048901</td>
      <td>489.01</td>
    </tr>
    <tr>
      <th>113</th>
      <td>POINT (441283.025 4477555.927)</td>
      <td>nuevos ministerios</td>
      <td>186</td>
      <td>L8</td>
      <td>1</td>
      <td>40.446550</td>
      <td>-3.692418</td>
      <td>4072.123003</td>
      <td>14.040057</td>
      <td>0.041374</td>
      <td>413.74</td>
    </tr>
    <tr>
      <th>34</th>
      <td>POINT (440361.306 4477979.100)</td>
      <td>alvarado</td>
      <td>8</td>
      <td>L1</td>
      <td>8</td>
      <td>40.450331</td>
      <td>-3.703318</td>
      <td>3110.129023</td>
      <td>13.620316</td>
      <td>0.052552</td>
      <td>525.52</td>
    </tr>
  </tbody>
</table>
</div>


```python
# Dibujar
fig,ax = plt.subplots(3,1,figsize=(10,20), sharex=True, sharey=True)

mrg.plot(
    column='sale', cmap='coolwarm', 
    ax=ax[0], legend=True, 
    legend_kwds={'label': 'Distribución del precio de venta en las lineas de metro', 'orientation': "horizontal"})
mrg.plot(
    column='rental', cmap='coolwarm',
    ax=ax[1], legend=True, 
    legend_kwds={'label': 'Distribución del precio de alquiler en las lineas de metro', 'orientation': "horizontal"})
mrg.plot(
    column='performance', cmap='coolwarm',
    ax=ax[2], legend=True, 
    legend_kwds={'label': 'Distribución del rendimiento de los inmuebles en las lineas de metro', 'orientation': "horizontal"})

plt.xticks([])
plt.yticks([])
plt.show()
```


    
![png](https://i.ibb.co/wKS1Y4m/output-16-0.png)
    


En los 3 plots mostrados arriba tenemos información de la distribución de los precios de venta y alquiler por metro cuadrado y también el rendimiento que tienen los inmuebles que están a 500 metros de cada parada de metro. Con dichas imágenes, somos capaces de tener un visión económica en función de la parada de metro que nos ayudará a tomar mejores decisiones en función de nuestras necesidades. 

<br>

### 4. Conclusiones<a name="4"></a>

Con esta práctica final se ha puesto en práctica lo aprendido durante el curso de Introducción a Python abordando los siguientes contenidos:

+ Leer un dataset desde una api
+ Analizar el conjunto de datos antes de proceder a su manipulación
+ Realizar un merge de dos dataframes y con los campos que nosotros queremos tratar
+ Añadir nuevas columnas a nuestro conjunto de datos
+ Graficar por medio de Matplotlib para sacar conclusiones
+ Formatear las gráficas para conseguir una mejor presentación de los datos
+ Usar Geopandas para las columnas que tienen latitud y longitud

Como nota final, recalcar que los datos que hemos usados no están actualizados por lo que no es recomendable que se tomen decisiones reales en base a esta práctica. 

<br>

### Enlaces y referencias<a name="5"></a>

+ <a href="https://www.adictosaltrabajo.com/2019/10/30/leer-un-json-con-r-rstudio/" target="_blank">Leer un JSON con R (RStudio)</a>
+ <a href="https://matplotlib.org/3.1.1/contents.html" target="_blank">Matplotlib</a>
+ <a href="https://idealista.carto.com/me" target="_blank">Datasets</a>
+ <a href="https://prod-hub-indexer.s3.amazonaws.com/files/f3859438e5504a6b9ca745880f72ef1b/0/full/25830/f3859438e5504a6b9ca745880f72ef1b_0_full_25830.zip" target="_blank">Geolalizaciones de las paradas de metro</a>

