import {shallowMount} from '@vue/test-utils'
import HelloJest from '@/HelloJest'

describe('HelloJest', () => {
    let wrapper
    beforeEach(() => {
        wrapper = shallowMount(HelloJest)
    })
    test('1', () => {
        expect(wrapper.vm.msg).toBe('Hello Jest!')
    })
})
