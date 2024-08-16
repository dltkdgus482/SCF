# base image
FROM node:20.15.0-alpine AS build

# set working directory
WORKDIR /app

# add /app/node_modules/.bin to $PATH
ENV PATH /app/node_modules/.bin:$PATH

# install app dependencies
COPY package.json ./
COPY package-lock.json ./
RUN npm install

# add app
COPY . ./

# build app
RUN npm run build

# stage to copy the build output to
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html

# custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# expose port 80
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
