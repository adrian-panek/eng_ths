FROM node:20.15-alpine as build
WORKDIR /app
COPY . /app/
RUN npm install
RUN npm run build

FROM nginx:1.27.0-alpine
COPY --from=build /app/dist/frontend/ /usr/share/nginx/html
RUN mv /usr/share/nginx/html/browser/** /usr/share/nginx/html/