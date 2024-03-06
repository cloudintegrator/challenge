FROM node:18-alpine

WORKDIR /app


ENV HOST="0.0.0.0"
ENV DISABLE_DEV_SERVER_HOST_CHECK=true
ENV HTTPS=false

COPY . .
USER 10014


RUN npm install
EXPOSE 3000
CMD ["npm", "start"]