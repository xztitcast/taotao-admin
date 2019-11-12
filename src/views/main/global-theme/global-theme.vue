<template>
  <div class="global-theme-btn-con">
    <Tooltip content="switch theme" theme="light">
      <Icon @click.native="handleLight" type="ios-water-outline" :size="23"/>
    </Tooltip>
  </div>
</template>
<script>
export default {
  name: 'GlobalTheme',
  methods: {
    handleLight () {

    }
  }
}
</script>
<style lang="less">
 .global-theme-btn-con .ivu-tooltip-rel{
   height: 64px;
   line-height: 56px;
   i{
     cursor: pointer;
   }
 }
</style>
